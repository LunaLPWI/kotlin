package com.example.luna_project.ui.theme.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.luna_project.ui.theme.LunaprojectTheme
import com.example.luna_project.ui.theme.components.AppointmentCard

class AppointmentActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LunaprojectTheme {
                AppointmentCard(
                    estabelecimento = "Barbearia do Zé",
                    status = "Ativo",
                    data = "13/05/2025",
                    horario = "15:30",
                    servico = "Barba",
                    barbeiro = "Derick",
                    valor = "R$ 70,00",
                    onCancel = null
                )

            }
        }

    }
}