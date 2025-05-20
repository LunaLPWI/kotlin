package com.example.luna_project.ui.theme.activities

import BarbershopViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.luna_project.MainScreen
import com.example.luna_project.ui.theme.LunaprojectTheme

class MainScreenActivityHome : ComponentActivity() {

    private lateinit var barbershopViewModel: BarbershopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val latitude = intent.getDoubleExtra("latitude", 37.4220936)
        val longitude = intent.getDoubleExtra("longitude", -122.083922)
        val clientId = intent.getLongExtra("clientId", -1L)

        Log.d("MainScreenActivityHome", "Latitude: $latitude, Longitude: $longitude, ClientId: $clientId")

        if (clientId == -1L) {
            Log.e("MainScreenActivityHome", "clientId inválido, encerrando activity")
            finish()
            return
        }
        barbershopViewModel = ViewModelProvider(this).get(BarbershopViewModel::class.java)

        barbershopViewModel.fetchBarbershops(latitude, longitude)

        setContent {
            LunaprojectTheme {
                MainScreen(clientId = clientId)
            }
        }
    }
}

