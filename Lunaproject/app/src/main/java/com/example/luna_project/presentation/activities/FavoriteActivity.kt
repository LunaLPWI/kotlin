package com.example.luna_project.ui.theme.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.luna_project.data.models.Barbershop
import com.example.luna_project.data.session.UserSession
import com.example.luna_project.ui.theme.FavoriteScreen

class FavoriteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clientId = UserSession.id
        val barbershops =
            intent.getSerializableExtra("barbershops") as? ArrayList<Barbershop> ?: arrayListOf()

        setContent {
            FavoriteScreen(clientId, barbershops)
        }
    }
}