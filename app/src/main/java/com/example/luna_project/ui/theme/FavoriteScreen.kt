package com.example.luna_project.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luna_project.BarberShopList
import com.example.luna_project.R
import com.example.luna_project.data.DTO.AddressDTO
import com.example.luna_project.data.models.Barbershop

@Composable
fun FavoriteScreen (){

    val mockBarbershopList = listOf(
        Barbershop(
            id = 1,
            name = "Dom Roque",
            addressDTO = AddressDTO(
                cep = "01234-000",
                logradouro = "Rua das Rosas",
                number = 123,
                complemento = "Sala 1",
                cidade = "São Paulo",
                bairro = "Centro",
                uf = "SP"
            ),
            planDTO = null,
            cnpj = "12.345.678/0001-99",
            openHour = "09:00",
            closeHour = "22:00",
            lat = -23.55052,
            logn = -46.633308
        ),
        Barbershop(
            id = 2,
            name = "Barbearia do Zé",
            addressDTO = AddressDTO(
                cep = "22290-000",
                logradouro = "Av. Central",
                number = 456,
                complemento = null,
                cidade = "Rio de Janeiro",
                bairro = "Copacabana",
                uf = "RJ"
            ),
            planDTO = null,
            cnpj = "98.765.432/0001-88",
            openHour = "10:00",
            closeHour = "20:00",
            lat = -22.9035,
            logn = -43.2096
        ),
        Barbershop(
            id = 3,
            name = "Barbearia Corte Fino",
            addressDTO = AddressDTO(
                cep = "80010-000",
                logradouro = "Rua Bela Vista",
                number = 789,
                complemento = "Loja 2",
                cidade = "Curitiba",
                bairro = "Batel",
                uf = "PR"
            ),
            planDTO = null,
            cnpj = "11.222.333/0001-44",
            openHour = "08:30",
            closeHour = "21:00",
            lat = -25.4284,
            logn = -49.2733
        )
    )

    Column(
    modifier = Modifier
    .fillMaxSize()
    .background(Color(0xFFF5F5F5))
    .padding(16.dp)
    ) {
        TopAppBarContent()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Favoritadas por Você!",
            style = MaterialTheme.typography.h6,
            color = Color(0xFF2E1A47)
        )
        Spacer(modifier = Modifier.height(16.dp))
        BarberShopList(mockBarbershopList)
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { /* ação do botão */ },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2E1A47))
        ) {
            Text(text = "Procurar Barbearias", color = Color.White)
        }
    }
}

@Composable
fun TopAppBarContent() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* voltar */ }) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }
        Text(text = "Luna Book", style = MaterialTheme.typography.h6)
        IconButton(onClick = { /* menu */ }) {
            Icon(Icons.Default.MoreVert, contentDescription = null)
        }
    }
}

@Composable
fun BarberCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFF2E1A47),
        modifier = Modifier
            .width(250.dp)
            .padding(end = 16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Dom Roque", color = Color.White, fontWeight = FontWeight.Bold)
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = "Barbearia",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("4.8 (238)", color = Color.White, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("9:00 - 22:00", color = Color.White, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Aberta agora", color = Color(0xFF4CAF50), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("2.3 Km", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}