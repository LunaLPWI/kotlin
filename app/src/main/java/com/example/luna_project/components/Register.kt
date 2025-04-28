package com.example.luna_project.components

import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luna_project.data.DTO.UserResponseLogin
import com.example.luna_project.ui.theme.activities.LoginActivity
import com.example.luna_project.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen() {
    val viewModel: RegisterViewModel = viewModel()
    val nome by viewModel.nome.collectAsState()
    val email by viewModel.email.collectAsState()
    val senha by viewModel.senha.collectAsState()
    val confirmarSenha by viewModel.confirmarSenha.collectAsState()
    val cpf by viewModel.cpf.collectAsState()
    val celular by viewModel.celular.collectAsState()
    val ddd by viewModel.ddd.collectAsState()

    val emailError by viewModel.emailError.collectAsState()
    val confirmarSenhaError by viewModel.confirmarSenhaError.collectAsState()
    val cpfError by viewModel.cpfError.collectAsState()


    val isFormValid = senha.isNotEmpty() &&
            isPasswordValid(senha) &&
            senha == confirmarSenha &&
            cpf.length == 11 &&
            !cpfError &&
            nome.isNotBlank() &&
            email.isNotBlank()

    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Criar conta",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(36, 12, 81),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = nome,
            onValueChange = { viewModel.onNomeChanged(it) },
            placeholder = "Nome completo"
        )

        CustomTextField(
            value = email,
            onValueChange = {
                viewModel.onEmailChanged(it.take(30)) // Atualiza o valor do email na ViewModel
            },
            placeholder = "Email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        if (emailError) {
            Text(
                text = "Email inválido, deve conter @",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        PasswordFieldWithCriteria(
            senha = senha,
            onSenhaChange = { newSenha ->
                viewModel.onSenhaChanged(newSenha) // Atualiza a senha na ViewModel
            }
        )

        CustomTextField(
            value = confirmarSenha,
            onValueChange = {
                viewModel.onConfirmarSenhaChanged(it) // Atualiza a confirmação de senha na ViewModel
            },
            placeholder = "Confirmar senha",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        if (confirmarSenhaError) {
            Text(
                text = "As senhas não coincidem.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        CustomTextField(
            value = cpf,
            onValueChange = { newCpf ->
                viewModel.onCpfChanged(newCpf) // Atualiza o CPF na ViewModel
            },
            placeholder = "CPF",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        if (cpfError) {
            Text(
                text = "CPF inválido",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }


        PhoneNumberWithDddSelector(
            phoneNumber = celular,
            onPhoneNumberChange = { newCelular ->
                viewModel.onCelularChanged(newCelular) // Atualiza o celular na ViewModel
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                    if (context is ComponentActivity) {
                        context.finish()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(),
                border = BorderStroke(1.dp, Color(36, 12, 81))
            ) {
                Text("Login", color = Color(36, 12, 81))
            }

            Spacer(modifier = Modifier.width(8.dp))

            val coroutineScope = rememberCoroutineScope()

            Button(
                onClick = {
                    coroutineScope.launch {  // <<< Agora usando rememberCoroutineScope
                        val sucesso = viewModel.cadastrarUsuario(context)

                        if (sucesso) {
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                            if (context is ComponentActivity) {
                                context.finish()
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(36, 12, 81))
            ) {
                Text("Cadastrar", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordFieldWithCriteria(senha: String, onSenhaChange: (String) -> Unit) {
    val senhaInteractionSource = remember { MutableInteractionSource() }
    val senhaFocusRequester = remember { FocusRequester() }
    var isSenhaFocused by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(false) }

    LaunchedEffect(senhaInteractionSource) {
        senhaInteractionSource.interactions.collect { interaction ->
            when (interaction) {
                is FocusInteraction.Focus -> {
                    isSenhaFocused = true
                }

                is FocusInteraction.Unfocus -> {
                    isSenhaFocused = false
                }
            }
        }
    }

    LaunchedEffect(senha) {
        isPasswordValid =
            isPasswordLengthValid(senha) && hasUppercase(senha) && hasDigit(senha) && hasSpecialChar(
                senha
            )
    }

    Column {
        if (isSenhaFocused && !isPasswordValid) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "A senha deve conter:",
                        fontWeight = FontWeight.Bold,
                        color = Color(36, 12, 81)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordRequirement(
                        "6 ou mais caracteres",
                        isPasswordLengthValid(senha)
                    )
                    PasswordRequirement(
                        "Pelo menos uma letra maiúscula",
                        hasUppercase(senha)
                    )
                    PasswordRequirement(
                        "Pelo menos um número",
                        hasDigit(senha)
                    )
                    PasswordRequirement(
                        "Pelo menos um Caractere especial",
                        hasSpecialChar(senha)
                    )
                }
            }
        }

        TextField(
            value = senha,
            onValueChange = { onSenhaChange(it) },
            placeholder = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            interactionSource = senhaInteractionSource,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(senhaFocusRequester),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF0F0F0),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        if (isPasswordValid) {
            Text(
                text = "Perfeito!",
                color = Color(0xFF05460A),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun PasswordRequirement(requirement: String, isValid: Boolean) {
    Text(
        text = requirement,
        color = if (isValid) Color.Green else Color.Red,
        fontSize = 12.sp
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(24.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF0F0F0),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        if (isError && isFocused) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberWithDddSelector(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    dddList: List<String> = listOf(
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "21", "22", "24", "27", "28",
        "31", "32", "33", "34", "35", "37", "38", "41", "42", "43", "44", "45", "46", "47", "48",
        "49", "51", "53", "54", "55", "61", "62", "63", "64", "65", "66", "67", "68", "69", "71",
        "73", "74", "75", "77", "79", "81", "82", "83", "84", "85", "86", "87", "88", "89", "91",
        "92", "93", "94", "95", "96", "97", "98", "99"
    )
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedDdd by remember { mutableStateOf(dddList.first()) }

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedDdd,
                onValueChange = { },
                label = { Text("DDD") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .width(100.dp),
                readOnly = true
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = 200.dp)
            ) {
                dddList.forEach { ddd ->
                    DropdownMenuItem(
                        text = { Text(ddd) },
                        onClick = {
                            selectedDdd = ddd
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { onPhoneNumberChange(it.filter { char -> char.isDigit() }.take(9)) },
            placeholder = { Text("Número") },
            label = { Text("Celular") },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPhoneNumberWithDddSelector() {
    PhoneNumberWithDddSelector(phoneNumber = "", onPhoneNumberChange = {})
}


fun isPasswordLengthValid(password: String): Boolean = password.length >= 6
fun hasUppercase(password: String): Boolean = password.contains(Regex(".*[A-Z].*"))
fun hasDigit(password: String): Boolean = password.contains(Regex(".*[0-9].*"))
fun hasSpecialChar(password: String): Boolean = password.contains(Regex(".*[!@#\$%^&*()_+=-].*"))

fun isPasswordValid(password: String): Boolean {
    return isPasswordLengthValid(password) && hasUppercase(password) && hasDigit(password) && hasSpecialChar(
        password
    )
}


//fun isCpfValid(cpf: String): Boolean {
//    if (cpf.length != 11 || cpf.all { it == cpf[0] }) return false
//
//    try {
//        val numbers = cpf.map { it.toString().toInt() }
//
//        val firstDigit = (0..8).sumOf { (10 - it) * numbers[it] } * 10 % 11 % 10
//        val secondDigit = (0..9).sumOf { (11 - it) * numbers[it] } * 10 % 11 % 10
//
//        return numbers[9] == firstDigit && numbers[10] == secondDigit
//    } catch (e: Exception) {
//        return false
//    }
//}