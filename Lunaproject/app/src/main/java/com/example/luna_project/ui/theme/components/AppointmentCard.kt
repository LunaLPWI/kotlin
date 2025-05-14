package com.example.luna_project.ui.theme.components
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults


@Composable
fun AppointmentCard(
    estabelecimento: String,
    status: String,
    data: String,
    horario: String,
    servico: String,
    barbeiro: String,
    valor: String,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFF2E004F), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text("Agenda", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)

        Spacer(modifier = Modifier.height(8.dp))

        Text(estabelecimento, fontWeight = FontWeight.Bold, color = Color.White)

        Text("Status: ", color = Color.White)
        Text(
            status,
            color = Color.Green,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Data: $data", color = Color.White)
        Text("Horário: $horario", color = Color.White)
        Text("Serviço: $servico", color = Color.White)
        Text("Barbeiro: $barbeiro", color = Color.White)
        Text("Valor: $valor", color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCancel,
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Cancelar", color = Color(0xFF2E004F))
        }
    }
}


