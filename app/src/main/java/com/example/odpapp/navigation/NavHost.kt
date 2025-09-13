package com.example.odpapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.odpapp.ui.theme.screens.ApiAuthScreen
import com.example.odpapp.ui.theme.screens.DatasetDetailScreen
import com.example.odpapp.ui.theme.screens.HomeScreen
import com.example.odpapp.ui.theme.screens.HomeViewModel
import com.example.odpapp.ui.theme.screens.DatasetRowDetailScreen
import com.example.odpapp.ui.theme.screens.FavoritesScreen
import com.example.odpapp.ui.theme.screens.SplashScreen
import com.example.odpapp.ui.theme.screens.StatisticsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("home") {
            val homeUiState by homeViewModel.homeUiState.collectAsState()
            HomeScreen(
                homeUiState = homeUiState,
                retryAction = homeViewModel::loadDatasets,
                navController = navController
            )
        }

        // Ažurirana ruta: Uklonjen URL
        composable(
            "datasetDetails/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) {
            // Screen sada sam kreira ViewModel, SavedStateHandle se automatski prosleđuje
            DatasetDetailScreen(navController = navController)
        }

        composable(
            "datasetRowDetail/{entityType}/{entityId}",
            arguments = listOf(
                navArgument("entityType") { type = NavType.StringType },
                navArgument("entityId") { type = NavType.IntType }
            )
        ) {
            DatasetRowDetailScreen()
        }

        composable("statistics") {
            StatisticsScreen()
        }

        composable("favorites") {
            FavoritesScreen(navController = navController)
        }

        composable("apiAuth") {
            ApiAuthScreen(navController)
        }

    }
}