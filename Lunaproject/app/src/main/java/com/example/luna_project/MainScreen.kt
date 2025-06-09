package com.example.luna_project

import BarberShopCard
import BarbershopViewModel
import LastVisitCard
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luna_project.data.models.Barbershop
import com.example.luna_project.presentation.components.RightDrawerContent
import com.example.luna_project.presentation.components.RightDrawerContentNotification
import com.example.luna_project.presentation.viewmodel.AssessmentViewModel
import com.example.luna_project.viewmodel.HomeViewModel
import fetchFavorites
import updateFavoriteInBackend
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(clientId: Long) {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel()
    val user by homeViewModel.user.collectAsState()
    val isDrawerOpen by homeViewModel.isDrawerOpen.collectAsState()
    val isDrawerNotificationOpen by homeViewModel.isNotificationDrawerOpen.collectAsState()
    val barbershopViewModel: BarbershopViewModel = viewModel()
    val assessmentViewModel: AssessmentViewModel = viewModel()
    val barbershops by barbershopViewModel.barbershops.collectAsState()
    val barbershopsSearch by barbershopViewModel.barbershopsSearch.collectAsState()
    val searchQuery = remember { mutableStateOf("") }
    val listToShow = if (searchQuery.value.isBlank()) barbershops else barbershopsSearch
    val lastScheduling by homeViewModel.lastScheduling.collectAsState()

    LaunchedEffect(Unit, user) {
        homeViewModel.loadUserSession(context)
        user?.let {
            homeViewModel.fetchLastScheduling(it.id)
        }
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

            SearchBarMain(
                modifier = Modifier.fillMaxWidth(),
                onSearchChanged = { query ->
                    searchQuery.value = query
                    barbershopViewModel.fetchSearchBaberShops(query)
                }
            )
//            LastVisitCard(lastScheduling)

            Text(
                text = "Barbearias Próximas",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )



            BarberShopList(clientId, listToShow)
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
                    .width(320.dp)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                RightDrawerContent(
                    clientId = clientId,
                    barbershops = listToShow,
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
                    onCloseDrawer = { homeViewModel.closeNotificationDrawer() },
                    assessmentViewModel,
                )
            }
        }
    }



}

@Composable
fun BarberShopList(clientId: Long, barbershops: List<Barbershop>) {
    val favoriteStates = remember { mutableStateMapOf<Long, Boolean>() }

    LaunchedEffect(clientId) {
        fetchFavorites(clientId) { fetchedFavorites ->
            favoriteStates.clear()
            favoriteStates.putAll(fetchedFavorites)
        }
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(barbershops.size) { index ->
            val barberShop = barbershops[index]
            BarberShopCard(
                clientId = clientId,
                barbershop = barberShop,
                barberName = barberShop.name,
                isFavorite = favoriteStates.getOrDefault(barberShop.id, false),
                onFavoriteChange = { isFav ->
                    favoriteStates[barberShop.id] = isFav
                    updateFavoriteInBackend(clientId, favoriteStates)
                }
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarMain(modifier: Modifier = Modifier, onSearchChanged: (String) -> Unit) {
    // Usando remember para armazenar o estado
    val text = remember { mutableStateOf("") }

    TextField(
        value = text.value,
        onValueChange = { newText ->
            text.value = newText
            onSearchChanged(newText) // Passa o novo valor para a função onSearchChanged
        },
        placeholder = { Text(text = "Pesquisar") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.padding(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}




