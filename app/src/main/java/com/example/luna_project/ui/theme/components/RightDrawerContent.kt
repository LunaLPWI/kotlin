package com.example.luna_project.ui.theme.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luna_project.data.models.SchedulingViewModel
import com.example.luna_project.data.session.UserSession
import com.example.luna_project.ui.theme.activities.FavoriteActivity
import com.example.luna_project.ui.theme.activities.LoginActivity
import com.example.luna_project.ui.theme.activities.ProfileActivity

@Composable
fun RightDrawerContent(onCloseDrawer: () -> Unit) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf("home") }

    when (currentScreen) {
        "home" -> {
            // Tela padrão do menu
            DrawerHomeContent(
                onCloseDrawer = onCloseDrawer,
                onAgendamentosClick = { currentScreen = "appointments" },
                onLogout = {
                    val intent = Intent(context, LoginActivity::class.java)
                    UserSession.clearSession()
                    context.startActivity(intent)
                }
            )
        }
        "appointments" -> {
            AppointmentsScreen(
                onBack = { currentScreen = "home" }
            )
        }
    }
}


@Composable
fun RightDrawerContentNotification(onCloseDrawer: () -> Unit) {
    var showNotification by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onCloseDrawer) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Fechar"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Notificações",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = showNotification,
            enter = slideInHorizontally(initialOffsetX = { -it }),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            NotificationCard(
                title = "Dom Roque Barbearia",
                message = "E aí, Pedro! Gostou do seu corte na Dom Roque? Responda com uma nota de 1 a 5 ⭐ para nos ajudar a melhorar!",
                onConfirm = { showNotification = false }
            )
        }
    }
}

@Composable
fun NotificationCard(title: String, message: String, onConfirm: () -> Unit) {
    var selectedStars by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFF2E004F), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        RatingStars(selectedStars) { stars -> selectedStars = stars }

        if (selectedStars > 0) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Text("Confirmar", color = Color(36, 12, 81))
            }
        }
    }
}

@Composable
fun RatingStars(selectedStars: Int, onRatingSelected: (Int) -> Unit) {
    Row {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star",
                tint = if (index < selectedStars) Color.Yellow else Color.White,
                modifier = Modifier
                    .padding(6.dp)
                    .clickable { onRatingSelected(index + 1) }
            )
        }
    }
}
@Composable
fun MenuButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E004F))
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = Color.White)
    }
}



@Composable
fun DrawerHomeContent(
    onCloseDrawer: () -> Unit,
    onAgendamentosClick: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onCloseDrawer) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Pedro Souza", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        MenuButton(icon = Icons.Default.Person, label = "Perfil") {
            context.startActivity(Intent(context, ProfileActivity::class.java))
        }

        MenuButton(icon = Icons.Default.CalendarToday, label = "Agendamentos", onClick = onAgendamentosClick)

        MenuButton(icon = Icons.Default.Favorite, label = "Favoritos") {
            context.startActivity(Intent(context, FavoriteActivity::class.java))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E004F))
        ) {
            Text("Sair", color = Color.White)
        }
    }
}


@Composable
fun AppointmentsScreen(onBack: () -> Unit) {
    val schedulingViewModel: SchedulingViewModel = viewModel()

    val listSchedulings by schedulingViewModel.schedulingsClients.collectAsState()
    LaunchedEffect(Unit) {

    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, contentDescription = "Voltar", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Agendamentos", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

//        // Simule múltiplos agendamentos
//        LazyColumn(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaçamento entre os itens
//        ) {
//            // Usamos forEach para iterar sobre os itens
//            items(barbershops.size) { index ->
//                val barbershop = barbershops[index]
//                BarberShopCard(barbershop) // Exibindo cada barbearia
//            }
//        }
        listSchedulings.forEach { scheduling ->

            AppointmentCard(
                estabelecimento = scheduling.stablishmentName,
                status = scheduling.status,
                data = scheduling.startDateTime,
                horario = scheduling.startDateTime,
                servico = scheduling.items.toString(),
                barbeiro = scheduling.nameEmployee,
                valor = "R$80,00",
                onCancel = { /* lógica de cancelamento */ }
            )
        }
    }
}
