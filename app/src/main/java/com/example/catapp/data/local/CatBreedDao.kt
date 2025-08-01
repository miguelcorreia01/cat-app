package com.example.catapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CatBreedDao {

    @Query("SELECT * FROM cat_breeds")
    fun getAllCatBreeds(): List<CatBreedEntity>

    @Query("SELECT * FROM cat_breeds WHERE id = :id")
    fun getCatBreedById(id: String): CatBreedEntity

    @Query("SELECT * FROM cat_breeds WHERE isFavorite = 1")
    fun getFavoriteCatBreeds(): List<CatBreedEntity>

    @Query("SELECT * FROM cat_breeds WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchCatBreeds(searchQuery: String): List<CatBreedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCatBreeds(catBreeds: List<CatBreedEntity>)

    @Update
    fun updateCatBreed(catBreed: CatBreedEntity)

    @Delete
    fun deleteCatBreed(catBreed: CatBreedEntity)

    @Query("SELECT * FROM cat_breeds")
    fun getAllCatBreedsFlow(): Flow<List<CatBreedEntity>>

    @Query("SELECT * FROM cat_breeds WHERE isFavorite = 1")
    fun getFavoriteCatBreedsFlow(): Flow<List<CatBreedEntity>>



}