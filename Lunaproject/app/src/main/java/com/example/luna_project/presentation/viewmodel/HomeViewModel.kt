package com.example.luna_project.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luna_project.data.models.ClientSchedulingDTOResponse
import com.example.luna_project.presentation.viewmodel.LoginViewModel
import com.example.luna_project.data.models.UserResponseLogin
import com.example.luna_project.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _user = MutableStateFlow<UserResponseLogin?>(null)
    val user: StateFlow<UserResponseLogin?> = _user

    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen

    private val _isNotificationDrawerOpen = MutableStateFlow(false)
    val isNotificationDrawerOpen: StateFlow<Boolean> = _isNotificationDrawerOpen

    private val loginViewModel = LoginViewModel()

    private val _lastScheduling = MutableStateFlow<ClientSchedulingDTOResponse?>(null)

    // Exposição somente leitura para a UI
    val lastScheduling: StateFlow<ClientSchedulingDTOResponse?> = _lastScheduling

    fun loadUserSession(context: Context) {
        _user.value = loginViewModel.getUserSession(context)
    }

    fun openDrawer() {
        _isDrawerOpen.value = true
    }

    fun closeDrawer() {
        _isDrawerOpen.value = false
    }

    fun openNotificationDrawer() {
        _isNotificationDrawerOpen.value = true
    }

    fun closeNotificationDrawer() {
        _isNotificationDrawerOpen.value = false
    }

    fun fetchLastScheduling(clientId: Long) {
        viewModelScope.launch {
            try {
                val userToken = "Bearer ${user.value?.token}"
                val response = RetrofitClient.apiService.getLastSchedulingClient(userToken, clientId)

                if (response.isSuccessful) {
                    response.body()?.let { dto ->
                        _lastScheduling.value = ClientSchedulingDTOResponse(
                            id = dto.id,
                            startDateTime = dto.startDateTime,
                            items = dto.items,
                            nameEmployee = dto.nameEmployee,
                            status = dto.status,
                            stablishmentName = dto.stablishmentName,
                            price = dto.price
                        )
                    } ?: Log.w("SchedulingFetch", "Agendamento não encontrado.")
                } else {
                    Log.e("SchedulingFetch", "Erro na resposta: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("SchedulingFetch", "Erro: ${e.localizedMessage}")
            }
        }
    }
}
