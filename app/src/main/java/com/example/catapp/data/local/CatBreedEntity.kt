package com.example.catapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat_breeds")
data class CatBreedEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val temperament: String,
    val origin: String,
    val lifeSpan: Int,
    var isFavorite: Boolean,
    val image: String
)
