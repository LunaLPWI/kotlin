package com.example.luna_project.presentation.components
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


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
        Text("Barbearia", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)

        Spacer(modifier = Modifier.height(8.dp))

        Text(estabelecimento, fontWeight = FontWeight.Bold, color = Color.White)


        Spacer(modifier = Modifier.height(8.dp))

        val rawDate = "2025-05-20T10:00:00" // ou a variável que vem com esse valor
        val formatterEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formatterSaida = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM", Locale("pt", "BR"))

        val parsedDate = LocalDateTime.parse(rawDate, formatterEntrada)
        val regex = Regex("""name=([^,]+)""") // captura tudo depois de name= até a próxima vírgula
        val nomes = regex.findAll(servico).map { it.groupValues[1].trim() }.toList()

// Resultado: ["Corte Masculino", "Barba"]
        val nomesFormatados = nomes.joinToString(separator = ", ")
        Text("📅 ${parsedDate.format(formatterSaida)}", color = Color.White)
        Text("Serviços: $nomesFormatados", color = Color.White)
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


