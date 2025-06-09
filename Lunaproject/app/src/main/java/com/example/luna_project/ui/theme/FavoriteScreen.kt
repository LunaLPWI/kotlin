package com.example.luna_project.ui.theme

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luna_project.BarberShopList
import com.example.luna_project.data.models.Barbershop
import com.example.luna_project.presentation.activities.MainActivity
import fetchFavoritesIds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(clientId: Long, barbershops: List<Barbershop>) {
    val context = LocalContext.current
    val favoriteIds = remember { mutableStateListOf<Long>() }
    val errorState = remember { mutableStateOf<Exception?>(null) }

    // Buscar favoritos ao abrir a tela
    LaunchedEffect(Unit) {
        try {
            val fetchedFavorites = fetchFavoritesIds(clientId)
            favoriteIds.clear()
            favoriteIds.addAll(fetchedFavorites)
        } catch (e: Exception) {
            errorState.value = e
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = {
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Luna Book", fontSize = 16.sp)
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            }
        )

        if (errorState.value != null) {
            Text("Falha ao carregar favoritos: ${errorState.value?.message}", color = Color.Red)
        } else {
            Text(
                text = "Favoritadas por Você!",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.h6,
                color = Color(0xFF2E1A47),
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Filtra apenas os favoritos usando a função auxiliar
            val favoriteBarbershops = filterFavoriteBarbershops(barbershops, favoriteIds)

            if (favoriteBarbershops.isEmpty()) {
                Text(
                    "Você não possui barbearias favoritas.",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            } else {
                BarberShopList(clientId, favoriteBarbershops) // passa só os favoritos!
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* Navegar para busca */ },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2E1A47))
            ) {
                Text(text = "Procurar Barbearias", color = Color.White)
            }
        }
    }
}

fun filterFavoriteBarbershops(
    barbershops: List<Barbershop>,
    favoriteIds: List<Long>
): List<Barbershop> {
    return barbershops.filter { it.id in favoriteIds }
}

