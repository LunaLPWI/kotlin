package com.example.luna_project.components

import android.content.Intent
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
import com.example.luna_project.ui.theme.activities.LoginActivity

@Composable
fun ResetPasswordScreen() {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var context = LocalContext.current

    val criteriaColors = listOf(
        if (newPassword.length >= 8) Color.Green else Color(36, 12, 81),
        if (newPassword.any { it.isLowerCase() }) Color.Green else Color(36, 12, 81),
        if (newPassword.any { it.isUpperCase() }) Color.Green else Color(36, 12, 81),
        if (newPassword.any { it.isDigit() }) Color.Green else Color(36, 12, 81),
        if (newPassword.any { !it.isLetterOrDigit() }) Color.Green else Color(36, 12, 81)
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
            Text(
                text = "◉ Deve conter, ao menos, 6 caracteres",
                fontSize = 14.sp,
                color = criteriaColors[0],
                lineHeight = 20.sp
            )
            Text(
                text = "◉ Deve conter, ao menos, uma letra minúscula",
                fontSize = 14.sp,
                color = criteriaColors[1],
                lineHeight = 20.sp
            )
            Text(
                text = "◉ Deve conter, ao menos, uma letra maiúscula",
                fontSize = 14.sp,
                color = criteriaColors[2],
                lineHeight = 20.sp
            )
            Text(
                text = "◉ Deve conter, ao menos, um número",
                fontSize = 14.sp,
                color = criteriaColors[3],
                lineHeight = 20.sp
            )
            Text(
                text = "◉ Deve conter, ao menos, um caractere especial",
                fontSize = 14.sp,
                color = criteriaColors[4],
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        ResetPasswordTextField(
            value = newPassword,
            onValueChange = {
                newPassword = it.take(10)
                isPasswordError = it != confirmPassword
                errorMessage = if (isPasswordError) "As senhas não coincidem" else ""
            },
            label = "Nova senha"
        )

        Spacer(modifier = Modifier.height(16.dp))

        ResetPasswordTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                isPasswordError = it != newPassword
                errorMessage = if (isPasswordError) "As senhas não coincidem" else ""
            },
            label = "Confirmar senha",
            isError = isPasswordError
        )

        if (isPasswordError) {
            Text(
                text = errorMessage,
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
                onClick = { },
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
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    isError: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, color = Color(0xFF240C51), fontSize = 16.sp)
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { if (placeholder.isNotEmpty()) Text(placeholder) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .border(BorderStroke(1.dp, if (isError) Color.Red else Color(0xFF240C51)), shape = RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Red
            )
        )
    }
}