import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luna_project.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luna_project.presentation.activities.MainScreenActivityHome

@Composable
fun ProfileScreen() {
    // Obtendo uma instância do ViewModel
    val profileViewModel: ProfileViewModel = viewModel()

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
                    val intent = Intent(context, MainScreenActivityHome::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)

                    (context as? Activity)?.finish()
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
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = "Foto do perfil",
                modifier = Modifier
                    .size(120.dp) // define o tamanho final
                    .clip(RoundedCornerShape(60.dp)) // borda arredondada
                    .aspectRatio(1f) // mantém a proporção quadrada
            )


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
                    ProfileField(
                        label = "Nome:",
                        hint = "ex: Enzo Valentino",
                        value = profileViewModel.name.value,
                        isEditable = profileViewModel.isNameEditable.value,
                        onValueChange = { profileViewModel.name.value = it },
                        onEditClick = { profileViewModel.toggleNameEditable() }
                    )
                    ProfileField(
                        label = "Número:",
                        hint = "ex: (99) 9999-999",
                        value = profileViewModel.phone.value,
                        isEditable = profileViewModel.isPhoneEditable.value,
                        onValueChange = { profileViewModel.phone.value = it },
                        onEditClick = { profileViewModel.togglePhoneEditable() }
                    )
                    ProfileField(
                        label = "E-mail:",
                        hint = "ex: email@gmail.com",
                        value = profileViewModel.email.value,
                        isEditable = profileViewModel.isEmailEditable.value,
                        onValueChange = { profileViewModel.email.value = it },
                        onEditClick = { profileViewModel.toggleEmailEditable() }
                    )
                    ProfileField(
                        label = "Senha:",
                        hint = "*******",
                        value = profileViewModel.password.value,
                        isEditable = profileViewModel.isPasswordEditable.value,
                        onValueChange = { profileViewModel.password.value = it },
                        onEditClick = { profileViewModel.togglePasswordEditable() }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            profileViewModel.confirmChanges()  // Aqui você pode chamar a função que salva ou valida os dados
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
fun ProfileField(
    label: String,
    hint: String,
    value: String,
    isEditable: Boolean,
    onValueChange: (String) -> Unit,
    onEditClick: () -> Unit
) {
    Row(modifier = Modifier.padding(vertical = 5.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = Color.White)
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(hint) },
                enabled = isEditable, // Controle de habilitação
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = if (isEditable) Color.White else Color.Gray.copy(alpha = 0.4f),
                    unfocusedContainerColor = if (isEditable) Color.White else Color.Gray.copy(alpha = 0.4f),
                    disabledContainerColor = Color.Gray.copy(alpha = 0.4f)
                )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar",
                tint = Color.White // Cor do ícone de editar
            )
        }
    }
}
