package com.ahmedapps.moviesapp.view.screens
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahmedapps.moviesapp.R
import com.ahmedapps.moviesapp.data.model.User
import com.ahmedapps.moviesapp.data.repository.FirebaseRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val name = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val showAlert = remember { mutableStateOf(false) }
    val alertMessage = remember { mutableStateOf("") }
    val firebaseRepository = FirebaseRepository()
    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }

    val showToast = remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun isPasswordValid(password: String): Boolean {
        val hasMinLetters = password.count { it.isLetter() } >= 4
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        return hasMinLetters && hasUpperCase && hasDigit && hasSpecialChar
    }

    LaunchedEffect(showToast.value) {
        if (showToast.value) {
            Toast.makeText(context, "Usuário criado com sucesso!", Toast.LENGTH_LONG).show()
            showToast.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Cadastro",
            fontSize = 50.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF8A56AC),
            modifier = Modifier
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Venha conhecer uma variedade de filmes com avaliações e muito mais!",
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier
                .padding( bottom = 32.dp)
                .align(alignment = Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Nome", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8A56AC)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            textStyle = TextStyle(fontSize = 18.sp),
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF8A56AC),
                unfocusedIndicatorColor = Color(0xFF8A56AC)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Nome de usuário", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8A56AC)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            textStyle = TextStyle(fontSize = 18.sp),
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF8A56AC),
                unfocusedIndicatorColor = Color(0xFF8A56AC)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Senha", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8A56AC)) },
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility.value)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(imageVector = image, contentDescription = null, tint = Color(0xFF8A56AC))
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            textStyle = TextStyle(fontSize = 18.sp),
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF8A56AC),
                unfocusedIndicatorColor = Color(0xFF8A56AC)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = { Text("Confirme a senha", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8A56AC)) },
            visualTransformation = if (confirmPasswordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisibility.value)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { confirmPasswordVisibility.value = !confirmPasswordVisibility.value }) {
                    Icon(imageVector = image, contentDescription = null, tint = Color(0xFF8A56AC))
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            textStyle = TextStyle(fontSize = 18.sp),
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF8A56AC),
                unfocusedIndicatorColor = Color(0xFF8A56AC)
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.value.isEmpty() || username.value.isEmpty() || password.value.isEmpty() || confirmPassword.value.isEmpty()) {
                    alertMessage.value = "Todos os campos devem ser preenchidos."
                    showAlert.value = true
                } else if (!isPasswordValid(password.value)) {
                    alertMessage.value = """
                        A senha deve conter:
                        - Pelo menos 4 letras
                        - Pelo menos 1 letra maiúscula
                        - Pelo menos 1 número
                        - Pelo menos 1 caractere especial
                    """.trimIndent()
                    showAlert.value = true
                } else if (password.value != confirmPassword.value) {
                    alertMessage.value = "As senhas não são iguais."
                    showAlert.value = true
                } else {
                    firebaseRepository.getUser(username.value) { existingUser ->
                        if (existingUser != null) {
                            alertMessage.value = "Nome de usuário já está em uso. Escolha outro."
                            showAlert.value = true
                        } else {
                            val user = User(name = name.value, username = username.value, password = password.value)
                            firebaseRepository.saveUser(username.value, user) { success ->
                                if (success) {
                                    showToast.value = true
                                    navController.navigate("login")
                                } else {
                                    alertMessage.value = "Erro ao cadastrar o usuário."
                                    showAlert.value = true
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8A56AC),
                contentColor = Color.White
            )
        ) {
            Text(text = "Cadastrar", fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Voltar para Login",
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    navController.navigate("login")
                }
                .padding(8.dp)
        )
    }

    if (showAlert.value) {
        AlertDialog(
            onDismissRequest = { showAlert.value = false },
            title = { Text("Erro no Cadastro") },
            text = { Text(alertMessage.value) },
            confirmButton = {
                Button(onClick = { showAlert.value = false }) {
                    Text("OK")
                }
            }
        )
    }
}
