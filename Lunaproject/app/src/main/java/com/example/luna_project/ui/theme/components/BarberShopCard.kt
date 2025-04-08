package com.example.luna_project.ui.theme.components

import com.example.luna_project.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BarberShopCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E004F)),
        modifier = Modifier
            .width(200.dp)
            .padding(horizontal = 8.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.barber_shop_image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Dom Roque",
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "4.8 (238) - 2.3 Km",
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }
    }
}