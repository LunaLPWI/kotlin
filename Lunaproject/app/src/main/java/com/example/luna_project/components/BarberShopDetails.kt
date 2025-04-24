package com.example.luna_project.components

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luna_project.R
import com.example.luna_project.ui.theme.activities.MainActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunaBookApp() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar() },
        sheetContent = {
            Image(
                painter = painterResource(R.drawable.barber_shop_image),
                contentDescription = "Barbearia",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
        },
        sheetPeekHeight = 0.dp
    ) {
        Content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Luna Book")
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }
        }
    )
}

@Composable
fun Content() {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar()
        Box(modifier = Modifier.weight(1f)) {
            // OsmdroidMapView() - Se necessário
        }
        BarberInfo()
    }
}

@Composable
fun SearchBar() {
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = { Text("Pesquisar") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun BarberInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(36, 12, 81).copy(alpha = 0.95f))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Dom Roque", color = Color.White, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("⭐ 4.8 (1238)", color = Color.White)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("9:00 - 22:00", color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Aberta agora", color = Color.Green)
                Spacer(modifier = Modifier.height(10.dp))
                Text("2.3 Km", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(16.dp)
    ) {
        Column {
            ButtonBar()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Endereço: R. Prof. Atílio Innocenti, 731 - Vila Nova Conceição, São Paulo - SP, 04538-001",
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Telefone: (11)95689-1314", color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(36, 12, 81))
            ) {
                Text("Agendar serviços", color = Color.White)
            }
        }
    }
}

@Composable
fun ButtonBar() {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            onClick = {},
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(36, 12, 81))
        ) {
            Text("Informações", color = Color.White)
        }
        OutlinedButton(
            onClick = {},
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(36, 12, 81))
        ) {
            Text("Avaliações", color = Color(36, 12, 81))
        }
    }
}
