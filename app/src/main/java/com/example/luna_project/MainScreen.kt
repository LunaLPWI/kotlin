package com.example.luna_project

import BarberShopCard
import BarbershopViewModel
import LastVisitCard
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luna_project.data.models.Barbershop
import com.example.luna_project.ui.theme.components.RightDrawerContent
import com.example.luna_project.ui.theme.components.RightDrawerContentNotification
import com.example.luna_project.ui.theme.components.SearchBar
import com.example.luna_project.viewmodel.HomeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel()

    val user by homeViewModel.user.collectAsState()
    val isDrawerOpen by homeViewModel.isDrawerOpen.collectAsState()
    val isDrawerNotificationOpen by homeViewModel.isNotificationDrawerOpen.collectAsState()
    val barbershopViewModel: BarbershopViewModel = viewModel()
    val barbershops by barbershopViewModel.barbershops.collectAsState()
    var barbershopsList =  barbershops
    var searchQuery = ""




    LaunchedEffect(Unit) {
        homeViewModel.loadUserSession(context)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 85.dp, start = 16.dp, end = 16.dp)
        ) {
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM", Locale("pt", "BR"))
            val formattedDate = currentDate.format(formatter)

            Text(
                text = "Olá, ${user?.name ?: ""} 👋",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = formattedDate,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                onSearchChanged = { query ->
                    searchQuery = query
                }
            )

            Text(
                text = "Última Visita",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LastVisitCard()

            Text(
                text = "Barbearias Próximas",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            BarberShopList(barbershopsList)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { homeViewModel.openNotificationDrawer() }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificação"
                )
            }

            IconButton(onClick = { homeViewModel.openDrawer() }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        }

        if (isDrawerOpen || isDrawerNotificationOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable {
                        homeViewModel.closeDrawer()
                        homeViewModel.closeNotificationDrawer()
                    }
            )
        }

        AnimatedVisibility(
            visible = isDrawerOpen,
            enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(100)),
            exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(100)),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(250.dp)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                RightDrawerContent(
                    onCloseDrawer = { homeViewModel.closeDrawer() }
                )
            }
        }

        AnimatedVisibility(
            visible = isDrawerNotificationOpen,
            enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(100)),
            exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(100)),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                RightDrawerContentNotification(
                    onCloseDrawer = { homeViewModel.closeNotificationDrawer() }
                )
            }
        }
    }



}

@Composable
fun BarberShopList(barbershops: List<Barbershop>) {

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaçamento entre os itens
    ) {
        // Usamos forEach para iterar sobre os itens
        items(barbershops.size) { index ->
            val barbershop = barbershops[index]
            BarberShopCard(barbershop) // Exibindo cada barbearia
        }
    }
}


