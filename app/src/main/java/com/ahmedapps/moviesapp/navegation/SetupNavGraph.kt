package com.ahmedapps.moviesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ahmedapps.moviesapp.view.screens.DetailsScreen
import com.ahmedapps.moviesapp.view.screens.LoginScreen
import com.ahmedapps.moviesapp.view.screens.MenuScreen
import com.ahmedapps.moviesapp.view.screens.ProfileScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ahmedapps.moviesapp.view.screens.RegisterScreen
import android.content.Context


@Composable
fun SetupNavGraph(navController: NavHostController, context: Context) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController, context = context)
        }
        composable("register") {
            RegisterScreen(navController = navController) // Remover context se não usado aqui
        }
        composable("menu") {
            MenuScreen(navController = navController, context = context)
        }
        composable("profile") {
            ProfileScreen(navController = navController, context = context)
        }
        composable(
            "details/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            DetailsScreen(movieId = movieId) // Ajuste para aceitar o movieId
        }
    }
}


