package com.example.luna_project.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.luna_project.data.models.CadastroResponse
import com.example.luna_project.data.network.RetrofitClient
import com.example.luna_project.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.String

class RegisterViewModel : ViewModel() {

    // Campos do formulário
    private val _nome = MutableStateFlow("")
    val nome: StateFlow<String> = _nome

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _senha = MutableStateFlow("")
    val senha: StateFlow<String> = _senha

    private val _confirmarSenha = MutableStateFlow("")
    val confirmarSenha: StateFlow<String> = _confirmarSenha

    private val _cpf = MutableStateFlow("")
    val cpf: StateFlow<String> = _cpf

    private val _celular = MutableStateFlow("")
    val celular: StateFlow<String> = _celular

    private val _ddd = MutableStateFlow("11")
    val ddd: StateFlow<String> = _ddd

    // Erros de validação
    private val _emailError = MutableStateFlow(false)
    val emailError: StateFlow<Boolean> = _emailError

    private val _confirmarSenhaError = MutableStateFlow(false)
    val confirmarSenhaError: StateFlow<Boolean> = _confirmarSenhaError

    private val _cpfError = MutableStateFlow(false)
    val cpfError: StateFlow<Boolean> = _cpfError

    suspend fun cadastrarUsuario(context: Context): Boolean {  // <<< agora retorna Boolean
        Log.d("MyApp", "Iniciando cadastro de usuário.")

        return try {
            Log.d("MyApp", "Preparando dados do usuário: nome=${_nome.value}, email=${_email.value}, cpf=${_cpf.value}")

            val usuario = User(
                id = null,
                name = _nome.value,
                email = _email.value,
                password = senha.value,
                cpf = _cpf.value,
                phoneNumber = "55${_ddd.value}${_celular.value}",
                birthDay = "2001-01-02",
                roles = emptySet()
            )

            Log.d("MyApp", "Chamando a API para cadastrar o usuário...")
            val response = sendToApi(usuario)

            if (response.isSuccessful) {
                Log.d("MyApp", "Cadastro realizado com sucesso: ${response.body()}")
                Toast.makeText(context, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                true
            } else {
                Log.e("MyApp", "Erro no cadastro: Código=${response.code()} Mensagem=${response.message()}")
                Toast.makeText(context, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show()
                false
            }
        } catch (e: Exception) {
            Log.e("MyApp", "Falha na conexão: ${e.message}", e)
            Toast.makeText(context, "Falha na conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            false
        }
    }

    // Função que faz a chamada HTTP
    private suspend fun sendToApi(usuario: User): Response<CadastroResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("MyApp", "Enviando requisição para API: $usuario")
                val response = RetrofitClient.apiService.cadastrarUsuario(usuario)
                Log.d("MyApp", "Resposta recebida: Código=${response.code()} Sucesso=${response.isSuccessful}")
                response
            } catch (e: Exception) {
                Log.e("MyApp", "Erro ao enviar requisição: ${e.message}", e)
                throw e
            }
        }
    }

    // Lógica de validação já existente
    fun isFormValid(): Boolean {
        return _nome.value.isNotBlank() &&
                _email.value.isNotBlank() &&
                !_emailError.value &&
                _senha.value == _confirmarSenha.value &&
                _cpf.value.length == 11 &&
                !_cpfError.value
    }

    // Atualizações de estado
    fun onNomeChanged(newNome: String) {
        _nome.value = newNome.take(30)
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail.take(30)
        _emailError.value = !(newEmail.contains("@") && newEmail.contains("."))
    }

    fun onSenhaChanged(newSenha: String) {
        _senha.value = newSenha
        validateConfirmarSenha()
    }

    fun onConfirmarSenhaChanged(newConfirmarSenha: String) {
        _confirmarSenha.value = newConfirmarSenha.take(30)
        validateConfirmarSenha()
    }

    private fun validateConfirmarSenha() {
        _confirmarSenhaError.value = _confirmarSenha.value.isNotEmpty() && _senha.value != _confirmarSenha.value
    }

    fun onCpfChanged(newCpf: String) {
        _cpf.value = newCpf.filter { it.isDigit() }.take(11)
        _cpfError.value = _cpf.value.length == 11 && !isCpfValid(_cpf.value)
    }

    fun onCelularChanged(newCelular: String) {
        _celular.value = newCelular.filter { it.isDigit() }.take(9)
    }

    private fun isCpfValid(cpf: String): Boolean {
        if (cpf.length != 11 || cpf.all { it == cpf[0] }) return false
        try {
            val numbers = cpf.map { it.toString().toInt() }
            val firstDigit = (0..8).sumOf { (10 - it) * numbers[it] } * 10 % 11 % 10
            val secondDigit = (0..9).sumOf { (11 - it) * numbers[it] } * 10 % 11 % 10
            return numbers[9] == firstDigit && numbers[10] == secondDigit
        } catch (e: Exception) {
            return false
        }
    }

//    fun onDddChanged(newDdd: String) {
//        _ddd.value = newDdd
//    }
//
//    fun isFormValid(): Boolean {
//        return _nome.value.isNotBlank() &&
//                _email.value.isNotBlank() &&
//                !_emailError.value &&
//                isPasswordValid(_senha.value) &&
//                _senha.value == _confirmarSenha.value &&
//                _cpf.value.length == 11 &&
//                !_cpfError.value
//    }

    // Funções auxiliares (você já tem essas)
//    private fun isPasswordValid(password: String): Boolean {
//        return isPasswordLengthValid(password) && hasUppercase(password) && hasDigit(password) && hasSpecialChar(password)
//    }

//    private fun isPasswordLengthValid(password: String): Boolean = password.length >= 6
//    private fun hasUppercase(password: String): Boolean = password.contains(Regex(".*[A-Z].*"))
//    private fun hasDigit(password: String): Boolean = password.contains(Regex(".*[0-9].*"))
//    private fun hasSpecialChar(password: String): Boolean = password.contains(Regex(".*[!@#\$%^&*()_+=-].*"))





}
