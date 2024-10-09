import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahmedapps.moviesapp.data.repository.FirebaseRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithMenu(navController: NavController, text: String) {
    var expanded by remember { mutableStateOf(false) }
    var showLogoutConfirmation by remember { mutableStateOf(false) }
    val context = LocalContext.current

    TopAppBar(
        title = { Text(text, fontSize = 26.sp, color = Color(0xFF8A56AC), fontWeight = FontWeight(600)) },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color(0xFF8A56AC),
                    modifier = Modifier
                        .size(32.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "Menu",
                            fontSize = 22.sp,
                            color = Color(0xFF8A56AC),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    },
                    onClick = {
                        expanded = false
                        navController.navigate("menu")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            "Editar Perfil",
                            fontSize = 22.sp,
                            color = Color(0xFF8A56AC),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    },
                    onClick = {
                        expanded = false
                        navController.navigate("profile")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            "Logout",
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onError,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    },
                    onClick = {
                        expanded = false
                        showLogoutConfirmation = true
                    },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.error)
                        .fillMaxWidth()
                )
            }
        }
    )

    if (showLogoutConfirmation) {
        LogoutConfirmationDialog(
            onConfirm = {
                FirebaseRepository().logout(context)
                navController.navigate("login") {
                    popUpTo("menu") { inclusive = true }
                }
                showLogoutConfirmation = false
            },
            onDismiss = {
                showLogoutConfirmation = false
            }
        )
    }
}

@Composable
fun LogoutConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Confirmação de Logout") },
        text = { Text("Tem certeza que deseja sair da sessão?") },
        confirmButton = {
            Button(
                onClick = { onConfirm() },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text("Sair")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}