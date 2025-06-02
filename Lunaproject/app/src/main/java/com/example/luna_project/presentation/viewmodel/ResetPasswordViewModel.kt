package com.example.luna_project.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.luna_project.data.models.ResetPasswordModel
import com.example.luna_project.data.network.RetrofitClient.apiService

class ResetPasswordViewModel : ViewModel() {

    // Removemos o `by` para garantir que estamos manipulando as propriedades de forma explícita
    var newPassword: MutableState<String> = mutableStateOf("")
        private set

    var confirmPassword: MutableState<String> = mutableStateOf("")
        private set

    var isPasswordError: MutableState<Boolean> = mutableStateOf(false)
        private set

    var errorMessage: MutableState<String> = mutableStateOf("")
        private set

    val criteriaColors: List<Boolean>
        get() = listOf(
            newPassword.value.length >= 8,
            newPassword.value.any { it.isLowerCase() },
            newPassword.value.any { it.isUpperCase() },
            newPassword.value.any { it.isDigit() },
            newPassword.value.any { !it.isLetterOrDigit() }
        )

    fun onNewPasswordChange(password: String) {
        newPassword.value = password
        validatePasswords()
    }

    fun onConfirmPasswordChange(password: String) {
        confirmPassword.value = password
        validatePasswords()
    }

    private fun validatePasswords() {
        isPasswordError.value = newPassword.value != confirmPassword.value
        errorMessage.value = if (isPasswordError.value) "As senhas não coincidem" else ""
    }

    suspend fun resetPassword(senha: String, email: String): Boolean {
        val resetPasswordModel = ResetPasswordModel(email, senha)
        return try {
            val resultMessage = apiService.resetPassword(resetPasswordModel)
            println("Senha alterada com sucesso! Resposta: $resultMessage")
            true
        } catch (e: Exception) {
            println("Falha: ${e.message}")
            false
        }
    }
}
