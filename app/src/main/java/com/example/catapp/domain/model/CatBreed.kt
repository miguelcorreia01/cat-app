package com.example.catapp.domain.model

data class CatBreed (
    val id: String,
    val name: String,
    val description: String,
    val temperament: String,
    val origin: String,
    val lifeSpan: Int,
    val isFavorite: Boolean,
    val image: String
)


