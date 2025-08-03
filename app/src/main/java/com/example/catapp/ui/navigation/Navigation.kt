package com.example.catapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catapp.ui.components.BottomBar
import com.example.catapp.ui.screens.breedDetails.BreedDetailScreen
import com.example.catapp.ui.screens.breedList.BreedListScreen
import com.example.catapp.ui.screens.favorites.FavoritesScreen

sealed class Screen(val route: String) {
    data object Breeds : Screen("breeds")
    data object Favorites : Screen("favorites")
    data object BreedDetail : Screen("breed_detail/{breedId}") {
        fun createRoute(breedId: String) = "breed_detail/$breedId"
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf("Breeds") }

    Scaffold(
        bottomBar = {
            BottomBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    when (tab) {
                        "Breeds" -> navController.navigate(Screen.Breeds.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        "Favorites" -> navController.navigate(Screen.Favorites.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Breeds.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Breeds.route) {
                BreedListScreen(
                    onBreedClick = { breedId ->
                        navController.navigate(Screen.BreedDetail.createRoute(breedId))
                    }
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onBreedClick = { breedId ->
                        navController.navigate(Screen.BreedDetail.createRoute(breedId))
                    }
                )
            }

            composable(Screen.BreedDetail.route) {
                BreedDetailScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}