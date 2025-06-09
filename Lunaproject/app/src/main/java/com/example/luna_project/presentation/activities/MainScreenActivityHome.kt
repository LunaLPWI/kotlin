package com.example.luna_project.presentation.activities

import BarbershopViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.luna_project.MainScreen
import com.example.luna_project.data.session.UserSession
import com.example.luna_project.ui.theme.LunaprojectTheme

class MainScreenActivityHome : ComponentActivity() {

    private lateinit var barbershopViewModel: BarbershopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pega a latitude e longitude passadas pela Intent
        val latitude = intent.getDoubleExtra("latitude", 37.4220936)
        val longitude = intent.getDoubleExtra("longitude", -122.083922)
        val clientId = UserSession.id

        Log.d("MainScreenActivityHome", "Latitude: $latitude, Longitude: $longitude")


        if (clientId == -1L) {
            Log.e("MainScreenActivityHome", "clientId inválido, encerrando activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Inicia o ViewModel para as barbearias
        barbershopViewModel = ViewModelProvider(this).get(BarbershopViewModel::class.java)

        // Chama a função para buscar as barbearias com as coordenadas
        barbershopViewModel.fetchBarbershops(latitude, longitude)

        setContent {
            LunaprojectTheme {
                MainScreen(clientId = clientId)
            }
        }
    }
}
