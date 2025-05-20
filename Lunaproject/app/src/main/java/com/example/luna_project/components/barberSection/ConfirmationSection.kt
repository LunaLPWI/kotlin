package com.example.luna_project.components.barberSection

import ServiceScreenViewModel
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luna_project.data.DTO.Barber
import com.example.luna_project.data.DTO.SchedulingRequestDTO
import com.example.luna_project.data.DTO.SchedulingResponseDTO
import com.example.luna_project.data.api.RetrofitClient
import com.example.luna_project.data.session.SelectBarberSession
import com.example.luna_project.data.session.UserSession
import com.example.luna_project.data.viewmodel.LoginViewModel
import com.example.luna_project.ui.theme.activities.MainScreenActivityHome
import com.example.luna_project.ui.theme.activities.SucessefullScreenActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.format.TextStyle


@Composable
fun ConfirmationSection(
    selectedDate: String,
    selectedTime: String,
    selectedServices: List<Triple<Long, String, Double>>,
    totalPrice: Double,
    selectedBarber: Barber?,
    onConfirm: () -> Unit,
    confirmationViewModel: ServiceScreenViewModel = viewModel()
) {
    LaunchedEffect(selectedDate, selectedTime, selectedServices, selectedBarber) {
        confirmationViewModel.updateReservationDetails(
            LocalDate.parse(selectedDate),
            selectedTime,
            selectedServices,
            selectedBarber
        )
    }

    val selectedDateFromViewModel = confirmationViewModel.selectedDate.value
    val selectedTimeFromViewModel = confirmationViewModel.selectedTime.value
    val selectedServicesFromViewModel = confirmationViewModel.selectedServices
    val totalPriceFromViewModel = confirmationViewModel.totalPrice.value
    val selectedBarberFromViewModel = confirmationViewModel.selectedBarber.value
    val loginViewModel = LoginViewModel()
    val context = LocalContext.current
    val user = loginViewModel.getUserSession(context)
    val userId = user?.id ?: -1
    val token = user?.token
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val data = LocalDate.parse(selectedDate, formatter)
    val activity = context as? Activity


    Column(modifier = Modifier.padding(16.dp)) {
        // Detalhes da Barbearia
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(SelectBarberSession.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                SelectBarberSession.addressDTO?.let { Text(it.formatAddress(), color = Color.Gray, fontSize = 14.sp) }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        "📅${data.format(DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM", Locale("pt", "BR")))}",
                        fontSize = 14.sp
                    )
                    Text("${selectedTimeFromViewModel }",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Subtotal
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Subtotal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                selectedServicesFromViewModel.forEach { (_, name, price) ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(name)
                        Text("R$ ${"%.2f".format(price)}")
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", fontWeight = FontWeight.Bold)
                    Text("R$ ${"%.2f".format(totalPriceFromViewModel)}", fontWeight = FontWeight.Bold)
                }
            }
        }
        // Faixa roxa inferior com data e hora
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(0xFF3E206D),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "📅${data.format(DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM", Locale("pt", "BR")))}",
                        color = Color.White
                    )
                    Text(
                        "$selectedTimeFromViewModel",
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botão confirmar
                Button(
                    onClick = {
                        val taskIds = selectedServices.map { it.first }
                        val formattedDateTime = "${selectedDateFromViewModel}T${selectedTimeFromViewModel}:00"

                        val dto = SchedulingRequestDTO(
                            clientId = userId,
                            employeeId = selectedBarberFromViewModel?.id ?: 0,
                            startDateTime = formattedDateTime,
                            items = taskIds
                        )

                        RetrofitClient.apiService.saveScheduling(dto, "Bearer $token")
                            .enqueue(object : retrofit2.Callback<SchedulingResponseDTO> {
                                override fun onResponse(
                                    call: retrofit2.Call<SchedulingResponseDTO>,
                                    response: retrofit2.Response<SchedulingResponseDTO>
                                ) {
                                    if (response.isSuccessful) {
                                        Log.d("ConfirmationSection", "Agendamento salvo com sucesso!")
                                        onConfirm()
                                        val intent = Intent(context, SucessefullScreenActivity::class.java)
                                        context.startActivity(intent)
                                        activity?.finish()
                                    } else {
                                        Log.e("ConfirmationSection", "Erro ao salvar agendamento: ${response.code()} ${dto} ${UserSession.token}")
                                    }
                                }

                                override fun onFailure(call: retrofit2.Call<SchedulingResponseDTO>, t: Throwable) {
                                    Log.e("ConfirmationSection", "Erro de rede: ${t.message}")
                                }
                            })
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text("Confirmar Reserva", color = Color.Black)
                }
            }
        }
    }
}

