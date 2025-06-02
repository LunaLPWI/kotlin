package com.example.luna_project.data.repository


import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest // A importação correta para permissões
import android.util.Log

class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationResult: (latitude: Double, longitude: Double) -> Unit) {
        Log.d("LocationHelper", "Tentando obter localização...")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("LocationHelper", "Permissões de localização não concedidas.")
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                Log.d("LocationHelper", "Localização obtida: Latitude: ${it.latitude}, Longitude: ${it.longitude}")
                onLocationResult(it.latitude, it.longitude)
            } ?: Log.e("LocationHelper", "Localização não encontrada.")
        }
    }
}
