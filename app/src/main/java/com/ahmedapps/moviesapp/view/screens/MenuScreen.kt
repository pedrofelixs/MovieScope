package com.ahmedapps.moviesapp.view.screens

import TopAppBarWithMenu
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahmedapps.moviesapp.data.repository.FirebaseRepository
import com.ahmedapps.moviesapp.movieList.eventState.MovieListUiEvent
import com.ahmedapps.moviesapp.viewModel.MovieListViewModel
import com.ahmedapps.moviesapp.movieList.util.Screen
import com.ahmedapps.moviesapp.utils.isLoggedIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavHostController, context: Context) {
    LaunchedEffect(Unit) {
        if (!isLoggedIn(context)) {
            navController.navigate("login") {
                popUpTo("menu") { inclusive = true }
            }
        }
    }

    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                bottomNavController = bottomNavController, onEvent = movieListViewModel::onEvent
            )
        },
        topBar = {
            TopAppBarWithMenu(
                navController = navController,
                text = if (movieListState.isCurrentPopularScreen) "Popular Movies" else "Upcoming Movies"
            )
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.PopularMovieList.rout
            ) {
                composable(Screen.PopularMovieList.rout) {
                    PopularMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
                composable(Screen.UpcomingMovieList.rout) {
                    UpcomingMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController, onEvent: (MovieListUiEvent) -> Unit
) {
    val items = listOf(
        BottomItem(
            title = "Popular",
            icon = Icons.Rounded.Movie
        ), BottomItem(
            title = "Upcoming",
            icon = Icons.Rounded.Upcoming
        )
    )

    val selected = remember { mutableStateOf(0) }

    NavigationBar {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(selected = selected.value == index, onClick = {
                    selected.value = index
                    when (selected.value) {
                        0 -> {
                            onEvent(MovieListUiEvent.Navigate)
                            bottomNavController.popBackStack()
                            bottomNavController.navigate(Screen.PopularMovieList.rout)
                        }

                        1 -> {
                            onEvent(MovieListUiEvent.Navigate)
                            bottomNavController.popBackStack()
                            bottomNavController.navigate(Screen.UpcomingMovieList.rout)
                        }
                    }
                }, icon = {
                    Icon(
                        imageVector = bottomItem.icon,
                        contentDescription = bottomItem.title,
                        tint = Color(0xFF8A56AC)
                    )
                }, label = {
                    Text(
                        text = bottomItem.title, color = Color(0xFF8A56AC), fontSize = 14.sp, fontWeight = FontWeight(700)
                    )
                })
            }
        }
    }
}

data class BottomItem(
    val title: String, val icon: ImageVector
)
