package com.example.luna_project.presentation.components.barberSection

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.luna_project.R
import com.example.luna_project.data.models.Barber
import com.example.luna_project.data.network.RetrofitClient
import com.example.luna_project.data.session.SelectBarberSession
import com.example.luna_project.data.session.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun BarbersSection(
    selectedBarbers: SnapshotStateList<Barber>,
    onBarberSelected: () -> Unit
) {
    val context = LocalContext.current
    val barbers = remember { mutableStateListOf<Barber>() }

    // Adicionando um log para indicar que a Composable foi iniciada
    Log.d("BarbersSection", "Composable iniciada")

    // Carregar dados da API
    LaunchedEffect(Unit) {
        Log.d("BarbersSection", "Iniciando carregamento de dados da API")
        RetrofitClient.apiService.getEmployeesById(SelectBarberSession.id, UserSession.token)
            .enqueue(object : Callback<List<Barber>> {
                override fun onResponse(
                    call: Call<List<Barber>>,
                    response: Response<List<Barber>>
                ) {
                    if (response.isSuccessful) {
                        val employees = response.body() ?: emptyList()
                        barbers.clear()
                        barbers.addAll(employees)
                        Log.d("BarbersSection", "Dados carregados com sucesso: ${employees.size} barbeiros")
                    } else {
                        Log.e("BarbersSection", "Falha ao carregar barbeiros: ${response.code()}")
                        Toast.makeText(context, "Falha ao carregar barbeiros", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Barber>>, t: Throwable) {
                    Log.e("BarbersSection", "Erro de rede: ${t.message}")
                    Toast.makeText(context, "Erro de rede: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    val selectedBarber = remember { mutableStateOf<Barber?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(barbers) { barber ->
                val isSelected = barber == selectedBarber.value
                BarberItem(
                    name = barber.name,
                    image = R.drawable.ic_barber,
                    selected = isSelected,
                    onClick = {
                        selectedBarber.value = if (isSelected) null else barber
                        Log.d("BarbersSection", "Barbeiro selecionado: ${barber.name}")
                    }
                )
            }
        }

        Button(
            onClick = {
                selectedBarber.value?.let {
                    selectedBarbers.clear()
                    selectedBarbers.add(it)
                    Log.d("BarbersSection", "Barbeiro confirmado: ${it.name}")
                    Toast.makeText(context, "Barbeiro escolhido", Toast.LENGTH_SHORT).show()
                    onBarberSelected()
                } ?: Toast.makeText(context, "Deve escolher um barbeiro", Toast.LENGTH_SHORT).show()
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
fun BarberItem(
    name: String,
    image: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (selected) Color(0xFFEEE4FF) else Color(0xFFF8F8F8),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter("drawable/ic_barber"),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.Black else Color.Gray
        )
    }
}
