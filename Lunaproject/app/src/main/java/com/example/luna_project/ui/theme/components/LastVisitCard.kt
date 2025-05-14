import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.luna_project.R

@Composable
fun LastVisitCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E004F)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_user),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(40.dp)
//                    .clip(RoundedCornerShape(20.dp)),
//                contentScale = ContentScale.Crop
//            )

            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text(text = "Derick Augusto", color = Color.White)
                Text(text = "Dom Roque", color = Color.Gray)
            }

            Button(onClick = {}) {
                Text("Reservar", color = Color.White)
            }
        }
    }
}