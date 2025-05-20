package com.example.luna_project.ui.theme.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.luna_project.data.repository.LocationHelper
import com.example.luna_project.data.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginViewModel = LoginViewModel()
        val user = loginViewModel.getUserSession(this)
        val userId = user?.id ?: -1

        if (userId.toInt() == -1) {
            Log.d("myApp", "Usuário não encontrado na sessão, redirecionando para o cadastro")
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d("myApp", "Usuário encontrado na sessão, redirecionando para a tela principal")

            // Inicializa o launcher de permissões
            requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                    if (isGranted) {
                        acessarLocalizacao(userId)
                    } else {
                        Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            // Verifique se a permissão de localização foi concedida
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                acessarLocalizacao(userId)
            }
        }
    }

    // Método que será chamado para acessar a localização depois que a permissão for concedida
    private fun acessarLocalizacao(userId: Long) {
        val locationHelper = LocationHelper(applicationContext)
        locationHelper.getCurrentLocation { latitude, longitude ->
            Log.d("Location", "Latitude: $latitude, Longitude: $longitude")

            // Passa a localização E o clientId para a próxima tela
            val intent = Intent(this, MainScreenActivityHome::class.java).apply {
                putExtra("latitude", latitude)
                putExtra("longitude", longitude)
                putExtra("clientId", userId) // <-- aqui é onde você passa o ID do cliente
            }
            startActivity(intent)
            finish()
        }
    }
}
