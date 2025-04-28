package com.example.luna_project.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luna_project.R
import com.example.luna_project.ui.theme.activities.MainActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current

    // Lista de serviços selecionados (nome e preço)
    val selectedServices = remember { mutableStateListOf<Pair<String, Double>>() }
    val selectedBarbers = remember { mutableStateListOf<Barber>() }

    var selectedBarber by remember { mutableStateOf<Barber?>(null) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedHour by remember { mutableStateOf<String?>(null) }
    var totalPrice by remember { mutableStateOf(0.0) }

    val selectedDateTime = remember { mutableStateOf<Pair<LocalDate?, String?>>(Pair(null, null)) }


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
                    androidx.compose.material3.Text(
                        "Luna Book",
                        fontSize = 16.sp
                    )
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

        HeaderSection()
        InfoSection()
        TabsSection(
            selectedTab = selectedTab,
            onTabSelected = { index -> selectedTab = index }
        )

        when (selectedTab) {
            0 -> ServicesSection(
                selectedServices = selectedServices,
                onServicesConfirmed = { selectedTab = 1
                    totalPrice = selectedServices.sumOf { it.second }}
            )

            1 -> BarbersSection(
                selectedBarbers = selectedBarbers,
                onBarberSelected = { selectedTab = 2 }
            )

            2 -> ReserveSection(
                selectedDateTime = selectedDateTime,
                onReserveSelected = { selectedTab = 3 }
            )

            3 ->  ConfirmationSection(
                selectedDate = selectedDateTime.value.first?.toString() ?: "",
                selectedTime = selectedDateTime.value.second ?: "",
                selectedServices = selectedServices,
                totalPrice = totalPrice,
                selectedBarber = selectedBarbers.firstOrNull(),
                onConfirm = {
                    if (selectedServices.isEmpty() || selectedBarbers.isEmpty() || selectedDateTime.value.first == null || selectedDateTime.value.second == null) {
                        Toast.makeText(context, "Preencha todos os campos para confirmar.", Toast.LENGTH_SHORT).show()
                    } else {

                        Toast.makeText(context, "Reserva Confirmada!", Toast.LENGTH_SHORT).show()
                    }
                }
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 1) {
            SelectBarberButton()
        }
    }
}


@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = com.example.luna_project.R.drawable.barber_shop_image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = { /* TODO: Voltar */ },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.Black)
        }
    }
}

@Composable
fun InfoSection() {
    Column(
        modifier = Modifier
            .background(Color(36, 12, 81))
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Dom Roque",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = "⭐ 4.8 (238)",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun TabsSection(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Serviços", "Barbeiros", "Reservar", "confirmar")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, title ->
            Button(
                onClick = { onTabSelected(index) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedTab == index) Color(36, 12, 81) else Color.White,
                    contentColor = if (selectedTab == index) Color.White else Color.Black
                ),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

data class Barber(val name: String, val image: Int)

@Composable
fun BarbersSection(
    selectedBarbers: SnapshotStateList<Barber>,
    onBarberSelected: () -> Unit
) {
    val context = LocalContext.current
    val barbers = listOf(
        Barber("Derick Augusto", R.drawable.ic_user),
        Barber("Leonardo Silva", R.drawable.ic_user),
        Barber("Gustavo Almeida", R.drawable.ic_user),
        Barber("Gabriel Souza", R.drawable.ic_user),
        Barber("Paulo Lima", R.drawable.ic_user)
    )

    val selectedBarber = remember { mutableStateOf<Barber?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                if (selected) Color(0xFF240C51) else Color(0xFFF0F0F0),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(100.dp)
                .padding(end = 16.dp)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column {
            Text(
                text = name,
                color = if (selected) Color.White else Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "⭐ 4.8(238)",
                color = if (selected) Color(0xFFFFFFFF) else Color(0xFF000000),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun SelectBarberButton() {
    Button(
        onClick = { },
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

@Composable
fun ServicesSection(
    selectedServices: MutableList<Pair<String, Double>>,
    onServicesConfirmed: () -> Unit
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("hair") }

    val hairServices = listOf(
        "Corte cabelo" to "R$ 40,00",
        "Corte Masculino" to "R$ 40,00",
        "Corte Masculino" to "R$ 40,00",
        "Corte Masculino" to "R$ 40,00"
    )

    val beardServices = listOf(
        "Corte de barba" to "R$ 30,00",
        "Bigode" to "R$ 20,00"
    )

    val razorServices = listOf(
        "Aparar navalha" to "R$ 35,00",
        "Desenho com navalha" to "R$ 50,00"
    )

    val services = when (selectedCategory) {
        "beard" -> beardServices
        "razor" -> razorServices
        else -> hairServices
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ServiceIcon(
                selected = selectedCategory == "hair",
                icon = R.drawable.ic_scissors,
                onClick = { selectedCategory = "hair" }
            )
            ServiceIcon(
                selected = selectedCategory == "beard",
                icon = R.drawable.ic_beard,
                onClick = { selectedCategory = "beard" }
            )
            ServiceIcon(
                selected = selectedCategory == "razor",
                icon = R.drawable.ic_razor,
                onClick = { selectedCategory = "razor" }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(services) { (title, priceString) ->
                val price = priceString.replace("R$ ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                val isSelected = selectedServices.any { it.first == title }

                ServiceItem(
                    title = title,
                    price = price,
                    selected = isSelected,
                    onClick = {
                        if (isSelected) {
                            selectedServices.removeIf { it.first == title }
                        } else {
                            selectedServices.add(title to price)
                        }
                    }
                )
            }
        }

        Button(
            onClick = {
                if (selectedServices.isNotEmpty()) {
                    Toast.makeText(context, "Serviços adicionados no carrinho", Toast.LENGTH_SHORT)
                        .show()
                    onServicesConfirmed()
                } else {
                    Toast.makeText(context, "Deve escolher um serviço", Toast.LENGTH_SHORT).show()
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
                text = "Selecionar Serviços",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun ServiceIcon(selected: Boolean, icon: Int, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(80.dp)
            .background(
                if (selected) Color(0xFFB3A2D5) else Color(0xFFF0F0F0),
                shape = CircleShape
            )
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun ServiceItem(title: String, price: Double, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (selected) Color(0xFFB3A2D5) else Color(0xFFF8F8F8),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = String.format("R$ %.2f", price),
                color = if (selected) Color.Black else Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}



@Composable
fun ReserveSection(
    selectedDateTime: MutableState<Pair<LocalDate?, String?>>, // Recebe o state
    onReserveSelected: () -> Unit
) {

    val context = LocalContext.current
    var currentMonthDate by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var showSelectionCard by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        MonthNavigation(currentMonthDate = currentMonthDate, onMonthChange = { currentMonthDate = it })

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle(title = "Data")

        DaysOfMonthRow(
            currentMonthDate = currentMonthDate,
            selectedDate = selectedDateTime.value.first, // Usa o state
            onDateSelected = { selectedDateTime.value = Pair(it, selectedDateTime.value.second) } // Atualiza o state
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle(title = "Hora")

        HoursRow(
            selectedHour = selectedDateTime.value.second, // Usa o state
            onHourSelected = { selectedDateTime.value = Pair(selectedDateTime.value.first, it) } // Atualiza o state
        )


        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            SelectionConfirmation(
                selectedDateTime.value.first, // Usa o state
                selectedDateTime.value.second, // Usa o state
                context,
                showSelectionCard
            )

            Button(
                onClick = {
                    if (selectedDateTime.value.first != null && selectedDateTime.value.second != null) {
                        showSelectionCard = true
                        val date = selectedDateTime.value.first
                        val hour = selectedDateTime.value.second
                        Toast.makeText(context, "Horário selecionado: $date às $hour", Toast.LENGTH_SHORT).show()
                        onReserveSelected()
                    } else {
                        Toast.makeText(context, "Deve escolher um horário", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(36, 12, 81)),
                shape = RoundedCornerShape(24.dp),
                elevation = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Selecionar Horário",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun MonthNavigation(
    currentMonthDate: LocalDate,
    onMonthChange: (LocalDate) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = currentMonthDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("pt", "BR"))),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onMonthChange(currentMonthDate.minusMonths(1).withDayOfMonth(1)) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
            IconButton(onClick = { onMonthChange(currentMonthDate.plusMonths(1).withDayOfMonth(1)) }) {
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
}

@Composable
private fun DaysOfMonthRow(
    currentMonthDate: LocalDate,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysInMonth = (1..currentMonthDate.lengthOfMonth()).map {
        currentMonthDate.withDayOfMonth(it)
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        items(daysInMonth.size) { index ->
            val date = daysInMonth[index]
            Button(
                onClick = { onDateSelected(date) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedDate == date) Color(0xFF240C51) else Color.White,
                    contentColor = if (selectedDate == date) Color.White else Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(60.dp)
                    .height(70.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = date.dayOfMonth.toString(), fontSize = 16.sp)
                    Text(
                        text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("pt-BR")),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun HoursRow(
    selectedHour: String?,
    onHourSelected: (String) -> Unit
) {
    val hours = listOf("09:00", "09:30", "10:00", "10:30")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(hours.size) { index ->
            val hour = hours[index]
            Button(
                onClick = { onHourSelected(hour) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedHour == hour) Color(0xFF240C51) else Color.White,
                    contentColor = if (selectedHour == hour) Color.White else Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(40.dp)
            ) {
                Text(text = hour)
            }
        }
    }
}

@Composable
private fun SelectionConfirmation(
    selectedDate: LocalDate?,
    selectedHour: String?,
    context: android.content.Context,
    showSelectionCard: Boolean
) {
    if (showSelectionCard && selectedDate != null && selectedHour != null) {
        Card(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            elevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp) // Adjust padding to avoid overlap. May need tweaking.
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM", Locale("pt", "BR"))),
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Text(
                    text = "$selectedHour - ${getNextHalfHour(selectedHour)}",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

fun getNextHalfHour(hour: String): String {
    val (hours, minutes) = hour.split(":").map { it.toInt() }
    val nextMinutes = (minutes + 30) % 60
    val nextHours = (hours + (minutes + 30) / 60) % 24
    return String.format("%02d:%02d", nextHours, nextMinutes)
}



@SuppressLint("DefaultLocale")
@Composable
fun ConfirmationSection(
    selectedDate: String,
    selectedTime: String,
    selectedServices: List<Pair<String, Double>>,
    totalPrice: Double,
    selectedBarber: Barber?,
    onConfirm: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp), // Ajuste o valor para arredondar mais ou menos
                elevation = 2.dp, // Adiciona uma leve sombra (opcional)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Detalhes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Dom Roque")
                        Text(text = selectedDate)
                    }
                    Text("Rua pitinga, Vila Ema")
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("")
                        Text(text = selectedTime)
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Subtotal", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    for (service in selectedServices) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(text = service.first)
                            Text(text = "R$ ${String.format("%.2f", service.second)}")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Total", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            text = "R$ ${String.format("%.2f", totalPrice)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        Button(
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(36, 12, 81)),
            border = BorderStroke(1.dp, Color(36, 12, 81)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp)
        ) {
            Text(
                text = "Confirmar Reserva",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
