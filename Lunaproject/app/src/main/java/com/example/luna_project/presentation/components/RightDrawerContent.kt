package com.example.luna_project.presentation.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luna_project.data.models.AssessmentResponse
import com.example.luna_project.data.models.Barbershop
import com.example.luna_project.data.session.UserSession
import com.example.luna_project.presentation.activities.LoginActivity
import com.example.luna_project.presentation.activities.ProfileActivity
import com.example.luna_project.presentation.viewmodel.AssessmentViewModel
import com.example.luna_project.presentation.viewmodel.SchedulingViewModel
import com.example.luna_project.ui.theme.activities.FavoriteActivity
import com.example.luna_project.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun RightDrawerContent(clientId: Long, barbershops: List<Barbershop>, onCloseDrawer: () -> Unit) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf("home") }

    when (currentScreen) {
        "home" -> {
            // Tela padrão do menu
            DrawerHomeContent(
                clientId = clientId,
                barbershops = barbershops,
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
fun RightDrawerContentNotification(
    onCloseDrawer: () -> Unit,
    assessmentViewModel: AssessmentViewModel,
) {
    val context = LocalContext.current
    val timestamp = remember { LocalDateTime.now() }
    val homeViewModel: HomeViewModel = viewModel()

    val user by homeViewModel.user.collectAsState()

    LaunchedEffect(user?.id) {
        assessmentViewModel.fetchAssessments(user?.token ?: "0", user?.id ?: 0, timestamp, context)
    }

    var notifications by remember { mutableStateOf<List<AssessmentResponse>>(emptyList()) }
    val assessmentsState by assessmentViewModel.assessments.collectAsState()

    LaunchedEffect(assessmentsState) {
        notifications = assessmentsState
    }

    val scope = rememberCoroutineScope()

    // Observa resultado da atualização para dar feedback e remover notificação
    val updateResult by assessmentViewModel.updateResult.collectAsState()

    LaunchedEffect(updateResult) {
        updateResult?.let { success ->
            if (success) {
                Toast.makeText(context, "Avaliação enviada com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Falha ao enviar avaliação", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onCloseDrawer) {
                Icon(Icons.Default.Close, contentDescription = "Fechar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Notificações", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(notifications, key = { it.assessmentId }) { notification ->
                var visible by remember { mutableStateOf(true) }

                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(initialOffsetX = { -it }),
                    exit = slideOutHorizontally(targetOffsetX = { it }),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    NotificationCard(
                        title = notification.establishmentName,
                        message = "E aí, Gostou? Responda com uma nota de 1 a 5 ⭐ para nos ajudar a melhorar!",
                        onConfirm = { rating, userMsg ->
                            // Chama updateAssessment
                            assessmentViewModel.updateAssessment(
                                notification.assessmentId, rating.toDouble(), userMsg,
                                (user?.token ?: 0).toString()
                            )
                            // Remove a notificação da lista
                            visible = false
                            scope.launch {
                                kotlinx.coroutines.delay(300)
                                notifications =
                                    notifications.filter { it.assessmentId != notification.assessmentId }
                            }
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCard(
    title: String,
    message: String,
    onConfirm: (rating: Int, message: String) -> Unit
) {
    var selectedStars by remember { mutableStateOf(0) }
    var userMessage by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = Color(0xFF2E004F),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            fontSize = 14.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        RatingStars(selectedStars) { stars -> selectedStars = stars }

        if (selectedStars > 0) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = userMessage,
                onValueChange = { userMessage = it },
                label = { Text("Escreva uma mensagem") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color(0xFF2E004F),
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color(0xFF2E004F),
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onConfirm(selectedStars, userMessage.text)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Text("Confirmar", color = Color(0xFF2E004F))
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
    clientId: Long,
    barbershops: List<Barbershop>,
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

        MenuButton(
            icon = Icons.Default.CalendarToday,
            label = "Agendamentos",
            onClick = onAgendamentosClick
        )

        MenuButton(icon = Icons.Default.Favorite, label = "Favoritos") {
            val intent = Intent(context, FavoriteActivity::class.java).apply {
                putExtra("clientId", clientId)
                putExtra("barbershops", ArrayList(barbershops))
            }
            context.startActivity(intent)
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
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        schedulingViewModel.fetchSchedulings(context)
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


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listSchedulings.size) { index ->
                val scheduling = listSchedulings[index]
                AppointmentCard(
                    estabelecimento = scheduling.stablishmentName,
                    status = scheduling.status,
                    data = scheduling.startDateTime,
                    horario = scheduling.startDateTime,
                    servico = scheduling.items.toString(),
                    barbeiro = scheduling.nameEmployee,
                    valor = "R$ ${scheduling.price}",
                    onCancel = {
                        schedulingViewModel.fetchCancelSchedulings(
                            context,
                            scheduling.id
                        ) { success, message ->
                            if (success) {
                                schedulingViewModel.fetchSchedulings(context)
                            }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}
