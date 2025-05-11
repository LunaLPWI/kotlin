package com.example.luna_project.components.barberSection

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.luna_project.R
import com.example.luna_project.data.DTO.Barber
import com.example.luna_project.data.api.RetrofitClient
import com.example.luna_project.data.session.SelectBarberSession
import com.example.luna_project.data.session.UserSession
import getNextHalfHour
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ReserveSection(
    selectedBarber: Barber?,
    onReserveSelected: () -> Unit,
    onDateTimeSelected: (LocalDate?, String?) -> Unit
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedHour by remember { mutableStateOf<String?>(null) }
    val hours: MutableList<String> = remember { mutableStateListOf() }

    // Configuração do formato de hora
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (selectedBarber != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Imagem do barbeiro com placeholder e imagem de erro
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = "URL da imagem ou resource",
                                placeholder = painterResource(id = R.drawable.ic_user), // Coloque aqui o recurso de placeholder
                                error = painterResource(id = R.drawable.ic_beard) // Coloque aqui o recurso de erro
                            ),
                            contentDescription = "Imagem do barbeiro",
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Gray, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(8.dp)) // Espaço entre a imagem e o nome

                        // Nome do barbeiro
                        Column {
                            Text(
                                text = selectedBarber.name,
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp)) // Espaço entre o nome e o nome da sessão

                            // Nome da sessão ou serviço
                            Text(
                                text = SelectBarberSession.name,
                                color = Color.Black.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                    }

                }
            }


        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Data",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val days = (0..5).map { LocalDate.now().plusDays(it.toLong()) }
            items(days.size) { index ->
                val date = days[index]

                Button(
                    onClick = {
                        selectedDate = date

                        // Inicia a requisição ao selecionar uma data
                        selectedBarber?.let { barber ->
                            val openHourFormatted = SelectBarberSession.openHour.format(timeFormatter)
                            val closeHourFormatted = SelectBarberSession.closeHour.format(timeFormatter)
                            val startDateTime = "${date}T${openHourFormatted}"
                            val endDateTime = "${date}T${closeHourFormatted}"

                            Log.d("BarbersSection", "Detalhes da Requisição: OpenHour - CloseHour = $startDateTime - $endDateTime, Barber ID = ${barber.id}, User ID = ${UserSession.id}, Token = ${UserSession.token}")

                            RetrofitClient.apiService.getVacantSchedules(
                                "${startDateTime}",   // Ex: "2025-04-28T08:00"
                                "${endDateTime}",  // Ex: "2025-04-28T18:00"
                                barber.id,
                                UserSession.id,
                                "Bearer ${UserSession.token}"
                            ).enqueue(object : retrofit2.Callback<Set<String>> {
                                override fun onResponse(
                                    call: retrofit2.Call<Set<String>>,
                                    response: retrofit2.Response<Set<String>>
                                ) {
                                    Log.d("BarbersSection", "${response.body()}")
                                    if (response.isSuccessful) {
                                        val vacantSchedules = response.body() ?: emptySet()
                                        hours.clear()
                                        vacantSchedules.forEach { schedule ->
                                            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME // ou use um personalizado se necessário

                                            val localDateTime = LocalDateTime.parse(schedule, formatter)
                                            val localTimeFormatted = localDateTime.toLocalTime().format(timeFormatter)
                                            hours.add(localTimeFormatted)
                                        }
                                        Log.d("BarbersSection", "Dados carregados com sucesso: ${vacantSchedules.size} horários disponíveis")
                                    } else {
                                        Log.e("BarbersSection", "Falha ao carregar horários: ${response.code()}")
                                    }
                                }

                                override fun onFailure(call: retrofit2.Call<Set<String>>, t: Throwable) {
                                    Log.e("BarbersSection", "Erro de rede: ${t.message}")
                                }
                            })
                            Log.e("BarbersSection", "${endDateTime}")
                            Log.e("BarbersSection", "${startDateTime}")
                        } ?: Log.e("BarbersSection", "Nenhum barbeiro selecionado")

                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (selectedDate == date) Color(0xFF240C51) else Color.White,
                        contentColor = if (selectedDate == date) Color.White else Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(width = 60.dp, height = 70.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = date.dayOfMonth.toString(), fontSize = 16.sp)
                        Text(
                            text = date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() },
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seção de Selecionar Horário
        Text(
            text = "Hora",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(hours.size) { index ->
                val hour = hours[index]
                Button(
                    onClick = { selectedHour = hour },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (selectedHour == hour) Color(0xFF240C51) else Color.White,
                        contentColor = if (selectedHour == hour) Color.White else Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(text = hour)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onDateTimeSelected(selectedDate, selectedHour)
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
                text = "Selecionar Horário",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}