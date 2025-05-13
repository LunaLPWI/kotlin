package com.example.luna_project.data.models

import androidx.lifecycle.ViewModel
import com.example.luna_project.data.DTO.ClientSchedulingDTOResponse
import com.example.luna_project.data.DTO.UserResponseLogin
import com.example.luna_project.data.api.RetrofitClient
import com.example.luna_project.data.session.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SchedulingViewModel : ViewModel(){

    private val _schedulingsClient = MutableStateFlow<List<ClientSchedulingDTOResponse>>(emptyList())
    val schedulingsClients: StateFlow<List<ClientSchedulingDTOResponse>> = _schedulingsClient

    private suspend fun fetchSchedulings() {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        try {
            val response = RetrofitClient.apiService.getSchedulingClient(
                token = UserSession.token,
                start = formattedDateTime,
                clientId = UserSession.id
            )

            if (response.isSuccessful) {
                response.body()?.let { schedulings ->
                    _schedulingsClient.value = schedulings
                }
            } else {
                println("Erro: ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }
}