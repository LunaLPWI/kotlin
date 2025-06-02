package com.example.luna_project.presentation.components

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luna_project.R
import com.example.luna_project.presentation.activities.LoginActivity
import com.example.luna_project.presentation.activities.ResetPasswordActivity

@Composable
fun ConfirmTokenUI() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.auth),
            contentDescription = null,
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Digite o código enviado ao seu email",
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        TokenInput(
            onTokenComplete = { token ->

            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val intent = Intent(context, ResetPasswordActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(45.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(36, 12, 81),
                contentColor = Color.White
            )
        ) {
            Text("Verificar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(45.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF4B0082)
            ),
            border = BorderStroke(1.dp, Color(36, 12, 81))
        ) {
            Text("Voltar ao Login")
        }
    }

}

@Composable
fun TokenInput(
    tokenLength: Int = 4,
    onTokenComplete: (String) -> Unit
) {
    var token by remember { mutableStateOf(List(tokenLength) { "" }) }
    val focusRequesters = remember { List(tokenLength) { FocusRequester() } }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 32.dp)
    ) {
        for (i in 0 until tokenLength) {
            OutlinedTextField(
                value = token[i],
                onValueChange = { value ->
                    if (value.length <= 1) {
                        val newToken = token.toMutableList()
                        newToken[i] = value
                        token = newToken

                        when {
                            value.isNotEmpty() && i < tokenLength - 1 -> {
                                focusRequesters[i + 1].requestFocus()
                            }

                            value.isEmpty() && i > 0 -> {
                                focusRequesters[i - 1].requestFocus()
                            }
                        }

                        if (token.all { it.isNotEmpty() }) {
                            onTokenComplete(token.joinToString(""))
                        }
                    }
                },
                modifier = Modifier
                    .width(55.dp)
                    .height(80.dp)
                    .focusRequester(focusRequesters[i]),
                singleLine = true,
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}
