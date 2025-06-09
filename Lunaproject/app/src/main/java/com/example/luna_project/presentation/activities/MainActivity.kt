package com.example.luna_project.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.luna_project.presentation.viewmodel.LoginViewModel
import com.example.luna_project.data.repository.LocationHelper
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.luna_project.data.session.UserSession

class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginViewModel = LoginViewModel()
        val user = loginViewModel.getUserSession(this)
        val userId = user?.id ?: -1
        UserSession.token = user?.token.toString()

        if (userId.toInt() == -1) {
            Log.d("myApp", "Usuário não encontrado na sessão, redirecionando para o cadastro")
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d("myApp", "Usuário encontrado na sessão, redirecionando para a tela principal")

            // Inicializa o launcher de permissões
            requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {

                    // Permissão concedida, acesse a localização
                    acessarLocalizacao()
                } else {
                    // Permissão negada
                    Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show()
                }
            }

            // Verifique se a permissão de localização foi concedida
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Solicite a permissão de localização
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {

                acessarLocalizacao()
            }
        }
    }

    // Método que será chamado para acessar a localização depois que a permissão for concedida
    private fun acessarLocalizacao() {
        val locationHelper = LocationHelper(applicationContext)
        locationHelper.getCurrentLocation { latitude, longitude ->

            Log.d("Location", "Latitude: $latitude, Longitude: $longitude")

            // Corrigindo a imutabilidade de latitude e longitude
            var latitude = latitude
            var longitude = longitude


            // Agora que temos a localização, passamos para a MainScreen ou outra Activity
            val intent = Intent(this, MainScreenActivityHome::class.java).apply {
                putExtra("latitude", latitude)
                putExtra("longitude", longitude)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun getUltimoAgendamento(){

    }
}
