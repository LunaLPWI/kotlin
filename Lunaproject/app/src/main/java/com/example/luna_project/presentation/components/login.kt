package com.example.luna_project.presentation.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luna_project.presentation.viewmodel.LoginViewModel
import com.example.luna_project.presentation.activities.ForgotPasswordActivity
import com.example.luna_project.presentation.activities.MainScreenActivityHome
import com.example.luna_project.presentation.activities.RegisterActivity
import kotlinx.coroutines.launch


@Composable
fun LoginScreen() {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val email by viewModel.email.collectAsState()
    val senha by viewModel.senha.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFAFAFA)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Comece Agora",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF240C51)
        )
        Spacer(modifier = Modifier.height(24.dp))

        EmailTextField(
            value = email,
            onValueChange = { viewModel.onEmailChange(it) }
        )
        PasswordTextField(
            value = senha,
            onValueChange = { viewModel.onSenhaChange(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Esqueci a senha",
            color = Color(0xFF240C51),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    context.startActivity(Intent(context, ForgotPasswordActivity::class.java))
                }
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        val sucesso = viewModel.fazerLogin(context)

                        if (sucesso) {
                            val intent = Intent(context, MainScreenActivityHome::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            context.startActivity(intent)

                            (context as? Activity)?.finish()
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF240C51)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Login", color = Color.White)
            }


            Button(
                onClick = {
                    val intent = Intent(context, RegisterActivity::class.java)
                    context.startActivity(intent)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(),
                border = BorderStroke(1.dp, Color(0xFF240C51)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Cadastrar", color = Color(0xFF240C51))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Text(text = "Senha", color = Color(0xFF240C51), fontSize = 16.sp)
        TextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = PasswordVisualTransformation(),
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTextField(value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = "E-mail", color = Color(0xFF240C51), fontSize = 16.sp)
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("ex: email@gmail.com") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
