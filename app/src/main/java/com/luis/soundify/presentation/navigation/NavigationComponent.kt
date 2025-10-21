package com.luis.soundify.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luis.soundify.presentation.artist.ArtistScreen
import com.luis.soundify.presentation.artist.ArtistViewModel
import com.luis.soundify.presentation.home.HomeScreen
import com.luis.soundify.presentation.logIn.LogInScreen
import com.luis.soundify.presentation.logIn.LogInViewModel

@Composable
fun NavigationComponent(
    logInViewModel: LogInViewModel,
    onGetStartedClick: () -> Unit,
) {
    val navController = rememberNavController()
    val artistViewModel: ArtistViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(Routes.Login.route) {
            LogInScreen(
                viewModel = logInViewModel,
                onGetStartedClick = onGetStartedClick,
                onNavigateToHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Home.route) {
            HomeScreen(
                onNavigateToArtist = { genre ->
                    artistViewModel.searchGenre(genre = genre)
                    navController.navigate(Routes.Artist.route)
                }
            )
        }

        composable(Routes.Artist.route) {
            ArtistScreen(
                artistViewModel = artistViewModel,
                onBackClick = { navController.popBackStack() },
                onArtistClick = {}
            )
        }
    }
}