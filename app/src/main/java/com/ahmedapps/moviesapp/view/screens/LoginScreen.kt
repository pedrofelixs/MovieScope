package com.ahmedapps.moviesapp.view.screens

import androidx.compose.ui.res.painterResource
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.ahmedapps.moviesapp.data.repository.FirebaseRepository
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.core.rememberInfiniteTransition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.ahmedapps.moviesapp.R

private const val PREF_NAME = "user_session"
private const val PREF_IS_LOGGED_IN = "is_logged_in"
private const val PREF_USERNAME = "username"

fun saveLoginSession(context: Context, username: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean(PREF_IS_LOGGED_IN, true)
    editor.putString(PREF_USERNAME, username)
    editor.apply()
}

fun isLoggedIn(context: Context): Boolean {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(PREF_IS_LOGGED_IN, false)
}

@Composable
fun HideSystemBars() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.isStatusBarVisible = false
        systemUiController.isNavigationBarVisible = false
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, context: Context) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val showAlert = remember { mutableStateOf(false) }
    val alertMessage = remember { mutableStateOf("") }
    val firebaseRepository = FirebaseRepository()
    val passwordVisibility = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isLoggedIn(context)) {
            navController.navigate("menu")
        }
    }

    HideSystemBars()

    val infiniteTransition = rememberInfiniteTransition()
    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.truelogo),
            contentDescription = "Logo MovieScope",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "MovieScope",
            fontSize = 50.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF8A56AC),
            modifier = Modifier
                .padding(top = 32.dp, bottom = 24.dp)
                .graphicsLayer(alpha = animatedAlpha)
        )

        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Nome de Usuário", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8A56AC)) },
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
                else
                    Icons.Filled.VisibilityOff

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


        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (username.value.isEmpty() || password.value.isEmpty()) {
                    alertMessage.value = "Por favor, preencha todos os campos."
                    showAlert.value = true
                } else {
                    firebaseRepository.getUser(username.value) { user ->
                        if (user == null) {
                            alertMessage.value = "Usuário não encontrado."
                            showAlert.value = true
                        } else if (user.password != password.value) {
                            alertMessage.value = "Senha incorreta."
                            showAlert.value = true
                        } else {
                            saveLoginSession(context, username.value)
                            navController.navigate("menu")
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
            Text(
                text = "Entrar",
                fontSize = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Criar uma conta",
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    navController.navigate("register")
                }
                .padding(8.dp)
        )
    }

    if (showAlert.value) {
        AlertDialog(
            onDismissRequest = { showAlert.value = false },
            title = { Text("Erro de Login") },
            text = { Text(alertMessage.value) },
            confirmButton = {
                Button(
                    onClick = { showAlert.value = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
