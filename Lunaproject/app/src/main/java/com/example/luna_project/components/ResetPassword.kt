package com.example.luna_project.components

import android.content.Intent
import android.widget.Toast
import kotlin.reflect.KFunction1
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.luna_project.data.session.ResetSenhaSection
import com.example.luna_project.data.viewmodel.ResetPasswordViewModel
import com.example.luna_project.ui.theme.activities.LoginActivity
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordScreen(viewModel: ResetPasswordViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val context = LocalContext.current

    val criteriaColors = listOf(
        if (viewModel.criteriaColors[0]) Color.Green else Color(36, 12, 81),
        if (viewModel.criteriaColors[1]) Color.Green else Color(36, 12, 81),
        if (viewModel.criteriaColors[2]) Color.Green else Color(36, 12, 81),
        if (viewModel.criteriaColors[3]) Color.Green else Color(36, 12, 81),
        if (viewModel.criteriaColors[4]) Color.Green else Color(36, 12, 81)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Redefinir Senha",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(36, 12, 81)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text("◉ Deve conter, ao menos, 8 caracteres", fontSize = 14.sp, color = criteriaColors[0], lineHeight = 20.sp)
            Text("◉ Deve conter, ao menos, uma letra minúscula", fontSize = 14.sp, color = criteriaColors[1], lineHeight = 20.sp)
            Text("◉ Deve conter, ao menos, uma letra maiúscula", fontSize = 14.sp, color = criteriaColors[2], lineHeight = 20.sp)
            Text("◉ Deve conter, ao menos, um número", fontSize = 14.sp, color = criteriaColors[3], lineHeight = 20.sp)
            Text("◉ Deve conter, ao menos, um caractere especial", fontSize = 14.sp, color = criteriaColors[4], lineHeight = 20.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        ResetPasswordTextField(
            value = viewModel.newPassword.value,
            onValueChange = viewModel::onNewPasswordChange,
            label = "Nova senha"
        )

        Spacer(modifier = Modifier.height(16.dp))

        ResetPasswordTextField(
            value = viewModel.confirmPassword.value,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = "Confirmar senha"
        )

        if (viewModel.isPasswordError.value) {  // Use .value para acessar o valor de MutableState
            Text(
                text = viewModel.errorMessage.value, // String é válido aqui
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(36, 12, 81)
                ),
                border = BorderStroke(1.dp, Color(36, 12, 81)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Voltar")
            }

            Button(
                onClick = {
                    // Chamando a função suspensa para redefinir a senha dentro da Coroutine
                    viewModel.viewModelScope.launch {
                        val email = ResetSenhaSection.email
                        val senha = viewModel.confirmPassword.value

                        // Aqui você chama a função suspensa
                        val success = viewModel.resetPassword(senha, email)

                        // Verificando a resposta e dando feedback ao usuário
                        if (success) {
                            // Se a senha foi alterada com sucesso, navega para o LoginActivity
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                        } else {
                            // Caso contrário, exibe uma mensagem de erro
                            Toast.makeText(context, "Falha ao redefinir senha. Tente novamente.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(36, 12, 81),
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Redefinir")
            }

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,  // Corrigido para o tipo correto
    label: String
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, color = Color(0xFF240C51), fontSize = 16.sp)
        TextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .border(BorderStroke(1.dp, Color(0xFF240C51)), shape = RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
