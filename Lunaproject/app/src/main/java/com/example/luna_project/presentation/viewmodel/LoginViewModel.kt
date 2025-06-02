package com.example.luna_project.presentation.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.luna_project.data.models.UserResponseLogin
import com.example.luna_project.data.network.RetrofitClient
import com.example.luna_project.data.models.UserLogin
import com.example.luna_project.data.session.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _senha = MutableStateFlow("")
    val senha: StateFlow<String> = _senha

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onSenhaChange(newSenha: String) {
        _senha.value = newSenha
    }

    suspend fun fazerLogin(context: Context): Boolean {
        if (email.value.isBlank() || senha.value.isBlank()) {
            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return false
        }

        val usuario = UserLogin(
            email = _email.value,
            password = _senha.value
        )

        return try {
            val response = sendToApi(usuario, context)
            if (response) {
                Toast.makeText(context, "Usuário Logado com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Erro ao Logar usuário", Toast.LENGTH_SHORT).show()
            }
            response
        } catch (e: Exception) {
            Toast.makeText(context, "Erro na conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private suspend fun sendToApi(usuario: UserLogin, context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.loginUsuario(usuario)
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        // Salva no SharedPreferences
                        saveUserSession(context, user)

                        // Salva diretamente no objeto UserSession
                        UserSession.id = user.id
                        UserSession.name = user.name
                        UserSession.email = user.email
                        UserSession.cpf = user.cpf
                        UserSession.token = user.token
                        UserSession.phoneNumber = user.phoneNumber
                        UserSession.birthDay = user.birthDay
                        UserSession.roles = user.roles
                    }
                }
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
    fun saveUserSession(context: Context, user: UserResponseLogin) {
        val sharedPref = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong("user_id", user.id)
            putString("user_name", user.name)
            putString("user_email", user.email)
            putString("user_cpf", user.cpf)
            putString("user_token", user.token)
            putString("user_phone", user.phoneNumber)
            putString("user_birth", user.birthDay)
            putString("user_roles", user.roles.joinToString(","))
            apply()
        }
    }

    fun getUserSession(context: Context): UserResponseLogin? {
        val sharedPref = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val id = sharedPref.getLong("user_id", -1L)

        if (id == -1L) {
            return null
        }

        return UserResponseLogin(
            id = id,
            name = sharedPref.getString("user_name", "") ?: "",
            email = sharedPref.getString("user_email", "") ?: "",
            cpf = sharedPref.getString("user_cpf", "") ?: "",
            token = sharedPref.getString("user_token", "") ?: "",
            phoneNumber = sharedPref.getString("user_phone", "") ?: "",
            birthDay = sharedPref.getString("user_birth", "") ?: "",
            roles = sharedPref.getString("user_roles", "")?.split(",") ?: emptyList()
        )
    }
}
