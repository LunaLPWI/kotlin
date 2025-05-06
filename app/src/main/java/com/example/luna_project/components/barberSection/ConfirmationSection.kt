package com.example.luna_project.components.barberSection

import ServiceScreenViewModel
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luna_project.data.DTO.Barber
import com.example.luna_project.data.DTO.SchedulingRequestDTO
import com.example.luna_project.data.DTO.SchedulingResponseDTO
import com.example.luna_project.data.api.RetrofitClient
import com.example.luna_project.data.session.UserSession
import com.example.luna_project.data.viewmodel.LoginViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
    var token = user?.token

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Data: ${selectedDateFromViewModel?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
        Text("Hora: $selectedTimeFromViewModel")
        Text("Preço Total: R$ ${totalPriceFromViewModel}")
        Text("Barbeiro: ${selectedBarberFromViewModel?.name ?: "Não selecionado"}")

        selectedServicesFromViewModel.forEach { (_, name, price) ->
            Text("$name - R$ $price")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Coletar apenas os IDs dos serviços
                val taskIds = selectedServices.map { it.first }

                // Formatar data + hora
                val formattedDateTime = "${selectedDateFromViewModel}T${selectedTimeFromViewModel}:00"


                val dto = SchedulingRequestDTO(
                    clientId = userId,
                    employeeId = selectedBarberFromViewModel?.id ?: 0,
                    startDateTime = formattedDateTime,
                    items = taskIds
                )


                RetrofitClient.apiService.saveScheduling(dto, "Bearer ${token}")
                    .enqueue(object : retrofit2.Callback<SchedulingResponseDTO> {
                        override fun onResponse(
                            call: retrofit2.Call<SchedulingResponseDTO>,
                            response: retrofit2.Response<SchedulingResponseDTO>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("ConfirmationSection", "Agendamento salvo com sucesso!")
                                onConfirm()
                            } else {
                                Log.e("ConfirmationSection", "Erro ao salvar agendamento: ${response.code()} ${dto} ${UserSession.token}")
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<SchedulingResponseDTO>, t: Throwable) {
                            Log.e("ConfirmationSection", "Erro de rede: ${t.message}")
                        }
                    })
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF240C51))
        ) {
            Text("Confirmar", color = Color.White)
        }
    }
}
