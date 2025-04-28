package com.example.luna_project.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.luna_project.data.viewmodel.LoginViewModel
import com.example.luna_project.data.DTO.UserResponseLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _user = MutableStateFlow<UserResponseLogin?>(null)
    val user: StateFlow<UserResponseLogin?> = _user

    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen

    private val _isNotificationDrawerOpen = MutableStateFlow(false)
    val isNotificationDrawerOpen: StateFlow<Boolean> = _isNotificationDrawerOpen

    private val loginViewModel = LoginViewModel()

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
}
