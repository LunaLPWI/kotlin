import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luna_project.R
import com.example.luna_project.components.barberSection.BarbersSection
import com.example.luna_project.components.barberSection.ReserveSection
import com.example.luna_project.components.barberSection.ServicesSection
import com.example.luna_project.ui.theme.activities.MainActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current

    val selectedBarbers = remember { mutableStateListOf<Barber>() }
    var selectedService by remember { mutableStateOf<Pair<String, String>?>(null) } // Continua aqui pra guardar a escolha

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = {
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Text(
                        "Luna Book",
                        fontSize = 16.sp
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                IconButton(onClick = { /* ações futuras */ }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = null)
                }
            }
        )

        HeaderSection()
        InfoSection()
        TabsSection(
            selectedTab = selectedTab,
            onTabSelected = { index -> selectedTab = index }
        )

        when (selectedTab) {
            0 -> ServicesSection(
                onServiceConfirmed = { service ->
                    selectedService = service // Aqui você salva o serviço escolhido
                    selectedTab = 1 // E muda pra próxima aba
                }
            )
            1 -> BarbersSection(
                selectedBarbers = selectedBarbers,
                onBarberSelected = { selectedTab = 2 }
            )
            2 -> ReserveSection(
                selectedBarber = null,
                onReserveSelected = { selectedTab = 3 }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 1) {
            SelectBarberButton()
        }
    }
}


@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .height(200.dp)
    ) {
        // COMENTA ISSO TEMPORARIAMENTE:
         Image(
            painter = painterResource(id = com.example.luna_project.R.drawable.barber_shop_image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
         )
    }
}


@Composable
fun InfoSection() {
    Column(
        modifier = Modifier
            .background(Color(36, 12, 81))
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Dom Roque",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = "⭐ 4.8 (238)",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun TabsSection(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Serviços", "Barbeiros", "Reservar", "confirmar")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, title ->
            Button(
                onClick = { onTabSelected(index) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedTab == index) Color(36, 12, 81) else Color.White,
                    contentColor = if (selectedTab == index) Color.White else Color.Black
                ),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

data class Barber(val name: String, val image: Int)




//@Composable
//fun BarberItem(name: String, image: Int, selected: Boolean, onClick: () -> Unit) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(
//                if (selected) Color(0xFF240C51) else Color(0xFFF0F0F0),
//                shape = RoundedCornerShape(12.dp)
//            )
//            .clickable(onClick = onClick)
//            .padding(10.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .width(80.dp)
//                .height(100.dp)
//                .padding(end = 16.dp)
//        ) {
//            Image(
//                painter = painterResource(id = image),
//                contentDescription = null,
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//        Column {
//            Text(
//                text = name,
//                color = if (selected) Color.White else Color.Black,
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp
//            )
//            Text(
//                text = "⭐ 4.8(238)",
//                color = if (selected) Color(0xFFFFFFFF) else Color(0xFF000000),
//                fontSize = 14.sp
//            )
//        }
//    }
//}

@Composable
fun SelectBarberButton() {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(36, 12, 81)),
        border = BorderStroke(1.dp, Color(36, 12, 81)),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp)
    ) {
        Text(
            text = "Selecionar Barbeiro",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}





// Função auxiliar para calcular o próximo horário
fun getNextHalfHour(hour: String): String {
    val (hours, minutes) = hour.split(":").map { it.toInt() }
    val nextMinutes = (minutes + 30) % 60
    val nextHours = (hours + (minutes + 30) / 60) % 24
    return String.format("%02d:%02d", nextHours, nextMinutes)
}