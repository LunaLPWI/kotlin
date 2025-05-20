package com.example.luna_project.data.models

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luna_project.data.DTO.ClientSchedulingDTOResponse
import com.example.luna_project.data.DTO.UserResponseLogin
import com.example.luna_project.data.api.RetrofitClient
import com.example.luna_project.data.session.UserSession
import com.example.luna_project.data.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SchedulingViewModel : ViewModel() {

    private val _schedulingsClient = MutableStateFlow<List<ClientSchedulingDTOResponse>>(emptyList())
    val schedulingsClients: StateFlow<List<ClientSchedulingDTOResponse>> = _schedulingsClient
    private val _date = MutableStateFlow("")
    val date: StateFlow<String> = _date

    fun fetchSchedulings(context: Context) {
        viewModelScope.launch {
            val loginViewModel = LoginViewModel()
            val user = loginViewModel.getUserSession(context)
            val userId = user?.id ?: return@launch
            val token = user?.token ?: return@launch

            val currentDateTime = LocalDateTime.now()

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)
            val _date = MutableStateFlow(formattedDateTime)
            _date.value = formattedDateTime

            Log.d("FetchSchedulings", "userId: $userId, token: $token Hours: $formattedDateTime")

            try {
                val response = RetrofitClient.apiService.getSchedulingClient(
                    token = "Bearer $token",
                    start = formattedDateTime,
                    clientId = userId
                )

                if (response.isSuccessful) {
                    response.body()?.let { schedulings ->
                        _schedulingsClient.value = schedulings
                    }
                } else {
                    println("Erro na resposta: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchCancelSchedulings(
        context: Context,
        id: Long,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val loginViewModel = LoginViewModel()
            val user = loginViewModel.getUserSession(context)
            val token = user?.token ?: return@launch

            try {
                val response = RetrofitClient.apiService.deleteScheduling(
                    token = "Bearer $token",
                    id = id,
                )

                if (response.isSuccessful) {
                    onResult(true, "Cancelado com sucesso!")
                    _schedulingsClient.value =  emptyList()
                } else {
                    onResult(false, "Erro ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false, "Erro de conexão: ${e.message}")
            }
        }
    }
}