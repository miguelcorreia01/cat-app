package com.example.catapp.domain.model

data class CatBreedsUiState(
    val breeds: List<CatBreed> = emptyList(),
    val filteredBreeds: List<CatBreed> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)