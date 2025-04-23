import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luna_project.MainScreen
import com.example.luna_project.R
import com.example.luna_project.ui.theme.activities.MainActivity

@Composable
fun ProfileScreen() {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ícone de fechar
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar"
                    )
                }
            }

            // Título
            Text(
                text = "Perfil",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Imagem do perfil
//            Image(
//                painter = painterResource(id = R.drawable.ic_user),
//                contentDescription = "Foto do perfil",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(120.dp)
//                    .clip(RoundedCornerShape(60.dp))
//            )

            // Card com informações
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xff240c51)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Pedro Souza", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(text = "(11)xxxxx-1314", color = Color.White)
                    Text(text = "Pedro@gmail.com", color = Color.White)
                    Text(text = "xxxxxxxxxxxxxxxx", color = Color.White)
                }
            }

            // Título "Editar"
            Text(
                text = "Editar",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp)
            )

            // Formulário
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF240c51)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileField(label = "Nome:", hint = "ex: Enzo Valentino")
                    ProfileField(label = "Número:", hint = "ex: (99) 9999-999")
                    ProfileField(label = "E-mail:", hint = "ex: email@gmail.com")
                    ProfileField(label = "Senha:", hint = "*******")
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(text = "Confirmar", color = Color.Black)
                    }
                }
            }
        }
    }
}


@Composable
fun ProfileField(label: String, hint: String) {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        Text(text = label, color = Color.White)
        OutlinedTextField(
            value = text,
            onValueChange = { newValue ->
                text = newValue
            },
            placeholder = { Text(hint) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            )
        )
    }
}