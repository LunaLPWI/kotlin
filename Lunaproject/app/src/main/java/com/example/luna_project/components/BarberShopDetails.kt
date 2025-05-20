package com.example.luna_project.ui.theme.components
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.example.luna_project.R
import com.example.luna_project.data.session.SelectBarberSession
import com.example.luna_project.data.session.UserSession
import com.example.luna_project.data.viewmodel.LoginViewModel
import com.example.luna_project.ui.theme.activities.MainActivity
import com.example.luna_project.ui.theme.activities.ServiceActivity
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunaBookApp() {

    var context = LocalContext.current
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
        Content(context)
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
fun Content(context: Context) {
    var showReviews by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            OsmdroidMapView(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(0f)
            )

            ExpandableBarberImage()
        }

        BarberInfo(showReviews, onShowReviewsChange = { showReviews = it }, context)
    }
}


@Composable
fun OsmdroidMapView(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        )
    }

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setMultiTouchControls(true)

                // Coordenadas
                val point = GeoPoint(SelectBarberSession.lat, SelectBarberSession.logn)

                // Centraliza o mapa
                controller.setZoom(19.0)
                controller.setCenter(point)

                // Adiciona marcador
                val marker = Marker(this).apply {
                    position = point
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = SelectBarberSession.name
                }
                overlays.add(marker)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clipToBounds()
    )
}




@Composable
fun BarberInfo(showReviews: Boolean, onShowReviewsChange: (Boolean) -> Unit, context: Context) {


    Column {
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
                    Text(SelectBarberSession.name, color = Color.White, fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("⭐ 4.8 (1238)", color = Color.White)
                }
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val openTimeParsed = LocalTime.parse(SelectBarberSession.openHour)
                val closeTimeParsed = LocalTime.parse(SelectBarberSession.closeHour)
                val openTime = openTimeParsed.format(formatter)
                val closeTime = closeTimeParsed.format(formatter)

                val now = LocalTime.now()
                val isOpen = now.isAfter(openTimeParsed) && now.isBefore(closeTimeParsed)

                Column(horizontalAlignment = Alignment.End) {
                    Text("$openTime - $closeTime", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isOpen) "Aberta agora" else "Fechada agora",
                        color = if (isOpen) Color.Green else Color.Red
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("2.3 Km", color = Color.White)
                }


            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                ButtonBar(showReviews, onShowReviewsChange)
                Spacer(modifier = Modifier.height(8.dp))

                if (showReviews) {
                    ReviewsContent()
                } else {
                    val address = SelectBarberSession.addressDTO

                    val formattedAddress = buildString {
                        append("${address?.logradouro}, ${address?.number}")
                        if (!address?.complemento.isNullOrBlank()) append(" - ${address.complemento}")
                        append("\n${address?.bairro}, ${address?.cidade} - ${address?.uf}")
                        append("\nCEP: ${address?.cep}")
                    }
                    Text(
                        formattedAddress,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Telefone: (11)95689-1314", color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val intent = Intent(context, ServiceActivity::class.java)
                            context.startActivity(intent)
                        },
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
    }
}


@Composable
fun ButtonBar(showReviews: Boolean, onShowReviewsChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            onClick = { onShowReviewsChange(false) },
            shape = RoundedCornerShape(16.dp),
            colors = if (!showReviews)
                ButtonDefaults.buttonColors(containerColor = Color(36, 12, 81))
            else
                ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Informações", color = if (!showReviews) Color.White else Color(36, 12, 81))
        }
        OutlinedButton(
            onClick = { onShowReviewsChange(true) },
            shape = RoundedCornerShape(16.dp),
            colors = if (showReviews)
                ButtonDefaults.buttonColors(containerColor = Color(36, 12, 81))
            else
                ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Avaliações", color = if (showReviews) Color.White else Color(36, 12, 81))
        }
    }
}

@Composable
fun ReviewsContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(reviewList) { review ->
                ReviewItem(
                    name = review.name,
                    rating = review.rating,
                    date = review.date,
                    review = review.text
                )
            }
        }
    }
}


@Composable
fun ReviewItem(name: String, rating: Int, date: String, review: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFF3F51B5), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(text = name, color = Color.White, fontSize = 20.sp)
        Text(text = "⭐".repeat(rating), color = Color.Yellow, fontSize = 16.sp)
        Text(text = date, color = Color.White, fontSize = 12.sp)
        Text(text = review, color = Color.LightGray, fontSize = 14.sp)
    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun ExpandableBarberImage() {
    val minHeight = 200.dp
    val maxHeight = 500.dp

    val density = LocalDensity.current
    val minOffset = with(density) { 320.dp.toPx() }
    val maxOffset = with(density) { -1.dp.toPx() }

    val offsetY = remember { Animatable(minOffset) }
    val coroutineScope = rememberCoroutineScope()

    val expandedHeight by derivedStateOf {
        lerp(minHeight, maxHeight, (minOffset - offsetY.value) / (minOffset - maxOffset))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(expandedHeight)
            .offset { IntOffset(0, offsetY.value.toInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val newOffset =
                            (offsetY.value + dragAmount.y).coerceIn(maxOffset, minOffset)
                        coroutineScope.launch {
                            offsetY.snapTo(newOffset)
                        }
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            if (offsetY.value < (maxOffset / 2)) {
                                offsetY.animateTo(maxOffset)
                            } else {
                                offsetY.animateTo(minOffset)
                            }
                        }
                    }
                )
            }
            .clip(RoundedCornerShape(24.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.barber_shop_image),
            contentDescription = "Imagem da Barbearia",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}


data class Review(val name: String, val rating: Int, val date: String, val text: String)


// Colocar dados do banco aqui!
val reviewList = listOf(
    Review("Derick Augusto", 5, "3 dias atrás", "Acompanhei um amigo, achei o lugar muito bom..."),
    Review("Gustavo Almeida", 5, "5 dias atrás", "Muito organizado e bem decorado..."),
    Review(
        "Lucas Prado",
        4,
        "1 semana atrás",
        "Serviço excelente, mas a espera foi um pouco longa."
    ),
    Review(
        "Fernanda Lopes",
        5,
        "2 semanas atrás",
        "Ambiente confortável e profissionais incríveis!"
    ),
    Review("Carla Dias", 4, "3 semanas atrás", "Gostei muito! Voltarei com certeza.")
)