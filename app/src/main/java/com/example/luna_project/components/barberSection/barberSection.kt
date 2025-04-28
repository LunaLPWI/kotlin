package com.example.luna_project.components.barberSection

import Barber
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luna_project.R

@Composable
fun BarbersSection(
    selectedBarbers: SnapshotStateList<Barber>,
    onBarberSelected: () -> Unit
) {
    val context = LocalContext.current
    val barbers = listOf(
        Barber("Derick Augusto", R.drawable.ic_barber),
    )

    val selectedBarber = remember { mutableStateOf<Barber?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(6.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(barbers) { barber ->
                val isSelected = barber == selectedBarber.value
                BarberItem(
                    name = barber.name,
                    image = barber.image,
                    selected = isSelected,
                    onClick = {
                        selectedBarber.value = if (isSelected) null else barber
                    }
                )
            }
        }

        Button(
            onClick = {
                if (selectedBarber.value != null) {
                    selectedBarbers.clear()
                    selectedBarbers.add(selectedBarber.value!!)

                    Toast.makeText(context, "Barbeiro escolhido", Toast.LENGTH_SHORT).show()
                    onBarberSelected()
                } else {
                    Toast.makeText(context, "Deve escolher um barbeiro", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(36, 12, 81)),
            border = BorderStroke(1.dp, Color(36, 12, 81)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp)
        ) {
            Text(
                text = "Selecionar Barbeiro",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }

}

    @Composable
    fun BarberItem(name: String, image: Int, selected: Boolean, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (selected) Color(0xFFB3A2D5) else Color(0xFFF8F8F8),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.White, shape = CircleShape)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.Black else Color.Gray
            )
        }
    }
