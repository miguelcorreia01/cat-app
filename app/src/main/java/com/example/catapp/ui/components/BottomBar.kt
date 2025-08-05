package com.example.catapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.shadow

@Composable
fun BottomBar(selectedTab: String, onTabSelected: (String) -> Unit) {

    NavigationBar(
        modifier = Modifier.shadow(14.dp, RectangleShape),
        containerColor = Color.White,
    ) {
        NavigationBarItem(
            selected = selectedTab == "Breeds",
            onClick = { onTabSelected("Breeds") },
            icon = { Icon(Icons.Filled.Pets, contentDescription = "Breeds") },
            label = { Text("Breeds") } ,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.DarkGray,
                unselectedIconColor = Color.LightGray,
                selectedTextColor = Color.DarkGray,
                unselectedTextColor = Color.LightGray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedTab == "Favorites",
            onClick = { onTabSelected("Favorites") },
            icon = { Icon(Icons.Filled.FavoriteBorder, contentDescription = "Favorites") },
            label = { Text("Favorites") } ,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.DarkGray,
                unselectedIconColor = Color.LightGray,
                selectedTextColor = Color.DarkGray,
                unselectedTextColor = Color.LightGray,
                indicatorColor = Color.Transparent
            )
        )

    }

}