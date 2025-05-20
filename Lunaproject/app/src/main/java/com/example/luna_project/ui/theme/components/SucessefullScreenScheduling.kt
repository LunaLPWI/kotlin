package com.example.luna_project.ui.theme.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.luna_project.ui.theme.activities.MainScreenActivityHome

@Composable
fun SucessefullScreenScheduling() {
    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = Color(0xFF2e004f),
            modifier = Modifier.size(96.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Serviço agendado",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Seu serviço foi agendado com sucesso",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                val intent = Intent(context, MainScreenActivityHome::class.java)
                context.startActivity(intent)
                activity?.finish() // Fecha a activity atual corretamente
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2e004f))
        ) {
            Text(text = "Voltar para tela inicial")
        }
    }
}
