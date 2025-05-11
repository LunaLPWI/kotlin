import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luna_project.R
import com.example.luna_project.data.models.Barbershop
import com.example.luna_project.data.session.SelectBarberSession
import com.example.luna_project.ui.theme.activities.BarberLocationActivity
import com.example.luna_project.ui.theme.activities.ServiceActivity
import com.example.luna_project.viewmodel.HomeViewModel

@Composable
fun BarberShopCard(barbershop: Barbershop) {
    val homeViewModel: HomeViewModel = viewModel()
    val user by homeViewModel.user.collectAsState()
    val context = LocalContext.current

    // Card com a exibição das informações da barbearia
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
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.barber_shop_image) // ou URL de imagem se disponível
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagem da barbearia",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Text(
                text = barbershop.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )

            // Exibe o endereço da barbearia
            Text(
                text = barbershop.addressDTO?.formatAddress() ?: "Endereço não disponível",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }
    }
}



