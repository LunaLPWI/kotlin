package com.example.luna_project.models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.luna_project.api.ApiService
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {
    var nome = mutableStateOf("")
    var email = mutableStateOf("")
    var senha = mutableStateOf("")
    var confirmarSenha = mutableStateOf("")
    var cpf = mutableStateOf("")
    var ddd = mutableStateOf("11")
    var celular = mutableStateOf("")

    private val apiService by lazy {
        criarRetrofit().create(ApiService::class.java)
    }

    fun registerUser(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!isFormValid()) {
            onError("Dados inválidos! Verifique os campos.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.cadastrarUsuario(
                    Usuario(nome.value, email.value, senha.value, cpf.value, ddd.value, celular.value)
                )
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Erro ao cadastrar: Código ${response.code()}")
                }
            } catch (e: HttpException) {
                onError("Erro HTTP ao tentar cadastrar: ${e.message()}")
            } catch (e: Exception) {
                onError("Erro desconhecido: ${e.localizedMessage ?: "Erro"}")
            }
        }
    }

    private fun isFormValid(): Boolean {
        return nome.value.isNotEmpty() &&
                email.value.isNotEmpty() &&
                email.value.contains("@") && email.value.contains(".") &&
                senha.value.isNotEmpty() &&
                senha.value == confirmarSenha.value &&
                cpf.value.length == 11 &&
                celular.value.isNotEmpty()
    }
}

fun criarRetrofit(): Retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:3000")
    .addConverterFactory(GsonConverterFactory.create())
    .build()