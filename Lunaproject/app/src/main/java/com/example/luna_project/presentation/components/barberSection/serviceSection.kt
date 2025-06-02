package com.example.luna_project.presentation.components.barberSection

import android.util.Log
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luna_project.R
import com.example.luna_project.data.models.Barber
import com.example.luna_project.data.network.RetrofitClient
import com.example.luna_project.data.session.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ServicesSection(
    selectedBarbers: SnapshotStateList<Barber>,
    onServiceConfirmed: (List<Triple<Long,String, Double>>) -> Unit
) {
    val context = LocalContext.current

    val allServices =  remember { mutableStateListOf<Triple<Long,String, Double>>() }



    LaunchedEffect(Unit) {
        Log.d("BarbersSection", "Iniciando carregamento de dados da API")
        RetrofitClient.apiService.getTasks(selectedBarbers[0].id, UserSession.token)
            .enqueue(object : Callback<List<Task>> {
                override fun onResponse(
                    call: Call<List<Task>>,
                    response: Response<List<Task>>
                ) {
                    if (response.isSuccessful) {
                        val tasks = response.body() ?: emptyList()
                        tasks.forEach { task ->
                            allServices.add(Triple(task.id,task.name, task.value))
                        }
                        Log.d("BarbersSection", "Dados carregados com sucesso: ${tasks.size} barbeiros")
                    } else {
                        Log.e("BarbersSection", "Falha ao carregar barbeiros: ${response.code()}")
                        Toast.makeText(context, "Falha ao carregar barbeiros", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                    Log.e("BarbersSection", "Erro de rede: ${t.message}")
                    Toast.makeText(context, "Erro de rede: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    val selectedServices = remember { mutableStateListOf<Triple<Long,String, Double>>() }

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
            items(allServices) { (id,title, price) ->
                val isSelected = selectedServices.any { it.second == title }

                ServiceItem(
                    title = title,
                    price = "R$ ${"%.2f".format(price)}",
                    selected = isSelected,
                    onClick = {
                        if (isSelected) {
                            selectedServices.removeAll { it.second == title }
                            Toast.makeText(context, "Serviço removido: $title", Toast.LENGTH_SHORT).show()
                        } else {
                            selectedServices.add(Triple(id, title, price))
                            Toast.makeText(context, "Serviço adicionado: $title", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }

        Button(
            onClick = {
                if (selectedServices.isNotEmpty()) {
                    onServiceConfirmed(selectedServices.toList())
                    Toast.makeText(context, "Serviços confirmados", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Selecione pelo menos um serviço", Toast.LENGTH_SHORT).show()
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
                text = "Selecionar Serviços",
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


data class Task(
    val id: Long,
    val name: String,
    val description: String,
    val value: Double,
    val duration: Int?
)