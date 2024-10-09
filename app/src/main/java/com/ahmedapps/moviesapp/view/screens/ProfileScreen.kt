package com.ahmedapps.moviesapp.view.screens

import TopAppBarWithMenu
import android.content.Context
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahmedapps.moviesapp.R
import com.ahmedapps.moviesapp.data.model.User
import com.ahmedapps.moviesapp.data.repository.FirebaseRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, context: Context) {
    val firebaseRepository = FirebaseRepository()
    val currentUsername = remember { mutableStateOf("") }
    val currentName = remember { mutableStateOf("") }
    val newUsername = remember { mutableStateOf("") }
    val newName = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val showAlert = remember { mutableStateOf(false) }
    val alertMessage = remember { mutableStateOf("") }
    val isEditing = remember { mutableStateOf(false) }
    val showDeleteConfirmation = remember { mutableStateOf(false) }

    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }

    fun isPasswordValid(password: String): Boolean {
        val hasMinLetters = password.count { it.isLetter() } >= 4
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        return hasMinLetters && hasUpperCase && hasDigit && hasSpecialChar
    }

    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", "") ?: ""

        if (savedUsername.isNotEmpty()) {
            firebaseRepository.getUser(savedUsername) { user ->
                if (user != null) {
                    currentUsername.value = user.username ?: ""
                    currentName.value = user.name ?: ""
                    newUsername.value = user.username ?: ""
                    newName.value = user.name ?: ""
                    newPassword.value = user.password ?: ""
                    confirmPassword.value = user.password ?: ""
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithMenu(navController = navController, text="Editar Perfil")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isEditing.value) {
                Text(text = "Informações Pessoais", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(32.dp))

                Text(text = "Nome: ${currentName.value}", fontSize = 22.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Username: ${currentUsername.value}", fontSize = 22.sp)
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { isEditing.value = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8A56AC),
                        contentColor = Color.White
                    )
                ){
                    Text(text = "Editar Perfil", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showDeleteConfirmation.value = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text(text = "Deletar Conta", fontSize = 18.sp)
                }
            } else {
                Row (
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                ){
                    Text(
                        text = "Bora lá, ${currentName.value.split(" ")[0]}!",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF8A56AC),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 24.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.truelogo),
                        contentDescription = "Logo MovieScope",
                        modifier = Modifier
                            .size(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = newUsername.value,
                    onValueChange = { newUsername.value = it },
                    label = { Text("Novo Username", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8A56AC)) },
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
                    value = newPassword.value,
                    onValueChange = { newPassword.value = it },
                    label = { Text("Nova Senha", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8A56AC)) },
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisibility.value)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        IconButton(onClick = {
                            passwordVisibility.value = !passwordVisibility.value
                        }) {
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
                    label = { Text("Confirmar Nova Senha", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8A56AC)) },
                    visualTransformation = if (confirmPasswordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confirmPasswordVisibility.value)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        IconButton(onClick = {
                            confirmPasswordVisibility.value = !confirmPasswordVisibility.value
                        }) {
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
                        if (newUsername.value.isEmpty() || currentName.value.isEmpty() || newPassword.value.isEmpty() || confirmPassword.value.isEmpty()) {
                            alertMessage.value = "Todos os campos devem ser preenchidos."
                            showAlert.value = true
                        } else if (!isPasswordValid(newPassword.value)) {
                            alertMessage.value = """
                                A senha deve conter:
                                - Pelo menos 4 letras
                                - Pelo menos 1 letra maiúscula
                                - Pelo menos 1 número
                                - Pelo menos 1 caractere especial
                            """.trimIndent()
                            showAlert.value = true
                        } else if (newPassword.value != confirmPassword.value) {
                            alertMessage.value = "As senhas não correspondem."
                            showAlert.value = true
                        } else {
                            firebaseRepository.getUser(newUsername.value) { user ->
                                if (user != null && user.username != currentUsername.value) {
                                    alertMessage.value = "Esse username já está em uso."
                                    showAlert.value = true
                                } else {
                                    val updatedUser = User(name = currentName.value, username = newUsername.value, password = newPassword.value)
                                    firebaseRepository.updateUser(currentUsername.value, updatedUser) { success ->
                                        if (success) {
                                            currentUsername.value = newUsername.value
                                            alertMessage.value = "Informações atualizadas com sucesso."
                                            showAlert.value = true
                                            isEditing.value = false
                                        } else {
                                            alertMessage.value = "Erro ao atualizar as informações."
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
                ){
                    Text(text = "Salvar Alterações", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Cancelar",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            isEditing.value = false
                        }
                        .padding(8.dp),
                    color = Color(0xFF8A56AC)
                )
            }
        }
    }

    if (showDeleteConfirmation.value) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation.value = false },
            title = { Text("Confirmar Deleção") },
            text = { Text("Tem certeza que deseja deletar sua conta? Esta ação não pode ser desfeita.") },
            confirmButton = {
                Button(
                    onClick = {
                        firebaseRepository.deleteUser(currentUsername.value) { success ->
                            if (success) {
                                firebaseRepository.logout(context)
                                navController.navigate("login") {
                                    popUpTo("menu") { inclusive = true }
                                }
                            } else {
                                alertMessage.value = "Erro ao deletar a conta."
                                showAlert.value = true
                            }
                        }
                        showDeleteConfirmation.value = false
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text("Deletar")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirmation.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showAlert.value) {
        AlertDialog(
            onDismissRequest = { showAlert.value = false },
            title = { Text("Aviso") },
            text = { Text(alertMessage.value) },
            confirmButton = {
                Button(onClick = { showAlert.value = false }) {
                    Text("OK")
                }
            }
        )
    }
}