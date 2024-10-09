package com.ahmedapps.moviesapp

import com.ahmedapps.moviesapp.view.screens.ProfileScreen

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahmedapps.moviesapp.view.screens.LoginScreen
import com.ahmedapps.moviesapp.view.screens.MenuScreen
import com.ahmedapps.moviesapp.view.screens.DetailsScreen // Importa a DetailsScreen
import com.ahmedapps.moviesapp.view.theme.MoviesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ahmedapps.moviesapp.view.screens.RegisterScreen
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SetupNavGraph(navController = navController, context = this@MainActivity)
                }
            }
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController, context: Context) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController = navController, context = context) }
        composable("register") { RegisterScreen(navController = navController) }
        composable("menu") { MenuScreen(navController = navController, context = context) }
        composable("profile") { ProfileScreen(navController = navController, context = context) }

        // Adiciona a rota para a DetailsScreen
        composable(
            "details/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") // Obter movieId como Int?
            DetailsScreen(movieId = movieId)
        }
    }
}
