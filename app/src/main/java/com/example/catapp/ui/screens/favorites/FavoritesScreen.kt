package com.example.catapp.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.catapp.ui.components.BreedGrid

@Composable
fun FavoritesScreen (
    viewModel: FavoritesViewModel = hiltViewModel(),
    onBreedClick: (String) -> Unit = {}
){
    val favorites by viewModel.favorites.collectAsState()
    val averageLifeSpan = viewModel.calculateAverageLifeSpan()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        FavoritesHeader(averageLifeSpan = averageLifeSpan)

        if(favorites.isEmpty()) {
            EmptyFavoritesScreen()
        } else {
            BreedGrid(
                breeds = favorites,
                onBreedClick = onBreedClick,
                onToggleFavorite = {viewModel.removeFromFavorites(it)
                }
            )
        }

    }
}

@Composable
fun FavoritesHeader(averageLifeSpan: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Favorite Breeds",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Average Life Span of your favorite breeds: ${averageLifeSpan.toInt()} years",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EmptyFavoritesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = "No favorite breeds yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}
