import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.luna_project.R
import com.example.luna_project.data.models.Barbershop
import com.example.luna_project.data.session.SelectBarberSession
import com.example.luna_project.java.FavoriteRepository
import com.example.luna_project.ui.theme.activities.BarberLocationActivity
import com.example.luna_project.viewmodel.HomeViewModel
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

@Composable
fun BarberShopCard(
    clientId: Long,
    barbershop: Barbershop,
    barberName: String,
    isFavorite: Boolean,
    onFavoriteChange: (Boolean) -> Unit
) {
    val homeViewModel: HomeViewModel = viewModel()
    val user by homeViewModel.user.collectAsState()
    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E004F)),
        modifier = Modifier
            .width(200.dp)
            .padding(horizontal = 8.dp)
            .clickable {
                SelectBarberSession.saveSelectedBarbershop(barbershop)
                val intent = Intent(context, BarberLocationActivity::class.java)
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.barber_shop_image)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagem da barbearia",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                IconButton(
                    onClick = {
                        val newState = !isFavorite
                        onFavoriteChange(newState)
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }

            Text(
                text = barbershop.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = barbershop.addressDTO?.formatAddress() ?: "Endereço não disponível",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }
    }
}

fun updateFavoriteInBackend(clientId: Long, favoriteMap: Map<Long, Boolean>) {
    val establishmentIds = favoriteMap.filterValues { it }.keys.toList()

    val requestBody = mapOf(
        "clientId" to clientId,
        "establishmentIds" to establishmentIds
    )

    val client = OkHttpClient()
    val mediaType = "application/json; charset=utf-8".toMediaType()
    val jsonBody = Gson().toJson(requestBody)
    val request = Request.Builder()
        .url("http://10.0.2.2:8080/clients/$clientId/favorites")
        .post(RequestBody.create(mediaType, jsonBody))
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("API", "Erro ao enviar favoritos", e)
        }

        override fun onResponse(call: Call, response: Response) {
            if (!response.isSuccessful) {
                Log.e("API", "Falha ao atualizar favoritos: ${response.code}")
            }
        }
    })
}





