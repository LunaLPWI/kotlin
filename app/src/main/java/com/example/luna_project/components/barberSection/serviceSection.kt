package com.example.luna_project.components.barberSection

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luna_project.R

@Composable
fun ServicesSection(
    onServiceConfirmed: (Pair<String, String>) -> Unit
) {
    val context = LocalContext.current

    val allServices = listOf(
        "Corte cabelo" to "R$ 40,00",
        "Corte Masculino" to "R$ 40,00",
        "Corte de barba" to "R$ 30,00",
        "Bigode" to "R$ 20,00",
        "Aparar navalha" to "R$ 35,00",
        "Desenho com navalha" to "R$ 50,00"
    )

    var currentSelectedService by remember { mutableStateOf<Pair<String, String>?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ServiceIcon(icon = R.drawable.ic_scissors)
            ServiceIcon(icon = R.drawable.ic_beard)
            ServiceIcon(icon = R.drawable.ic_razor)
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(allServices) { (title, price) ->
                val isSelected = currentSelectedService?.first == title
                ServiceItem(
                    title = title,
                    price = price,
                    selected = isSelected,
                    onClick = {
                        currentSelectedService = title to price
                        Toast.makeText(context, "Serviço selecionado: $title", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        Button(
            onClick = {
                if (currentSelectedService != null) {
                    Toast.makeText(context, "Serviço confirmado: ${currentSelectedService!!.first}", Toast.LENGTH_SHORT).show()
                    onServiceConfirmed(currentSelectedService!!)
                } else {
                    Toast.makeText(context, "Deve escolher um serviço", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(36, 12, 81)),
            border = BorderStroke(1.dp, Color(36, 12, 81)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp)
        ) {
            Text(
                text = "Selecionar Serviço",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ServiceItem(title: String, price: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (selected) Color(0xFF240C51) else Color(0xFFF0F0F0),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = price,
                fontSize = 14.sp,
                color = if (selected) Color.White else Color.Gray
            )
        }
    }
}

@Composable
fun ServiceIcon(icon: Int) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(Color.LightGray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }
}