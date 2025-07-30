package com.example.catapp.domain.repository

import com.example.catapp.domain.model.CatBreed
import kotlinx.coroutines.flow.Flow

interface CatBreedRepository {
    suspend fun getCatBreeds(): List<CatBreed>
    suspend fun getCatBreedById(id: String): CatBreed
    fun getFavoriteCatBreeds(): Flow<List<CatBreed>>
    suspend fun addCatBreedToFavorites(catBreed: CatBreed)
    suspend fun removeCatBreedFromFavorites(catBreed: CatBreed)

}