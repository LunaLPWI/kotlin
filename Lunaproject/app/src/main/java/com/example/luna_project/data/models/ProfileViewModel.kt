import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    // Usando MutableState explicitamente
    var name: MutableState<String> = mutableStateOf("")
    var phone: MutableState<String> = mutableStateOf("")
    var email: MutableState<String> = mutableStateOf("")
    var password: MutableState<String> = mutableStateOf("")

    var isNameEditable: MutableState<Boolean> = mutableStateOf(false)
    var isPhoneEditable: MutableState<Boolean> = mutableStateOf(false)
    var isEmailEditable: MutableState<Boolean> = mutableStateOf(false)
    var isPasswordEditable: MutableState<Boolean> = mutableStateOf(false)

    // Função para alternar a editabilidade do nome
    fun toggleNameEditable() {
        isNameEditable.value = !isNameEditable.value
    }

    // Funções semelhantes para os outros campos
    fun togglePhoneEditable() {
        isPhoneEditable.value = !isPhoneEditable.value
    }

    fun toggleEmailEditable() {
        isEmailEditable.value = !isEmailEditable.value
    }

    fun togglePasswordEditable() {
        isPasswordEditable.value = !isPasswordEditable.value
    }

    fun confirmChanges() {
        // Lógica para salvar ou validar as mudanças
    }
}
