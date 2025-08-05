package com.example.catapp.data.local

import androidx.paging.PagingSource
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
    suspend fun getAllCatBreeds(): List<CatBreedEntity>

    @Query("SELECT * FROM cat_breeds WHERE id = :id")
    suspend fun getCatBreedById(id: String): CatBreedEntity

    @Query("SELECT * FROM cat_breeds WHERE isFavorite = 1")
    suspend fun getFavoriteCatBreeds(): List<CatBreedEntity>

    @Query("SELECT * FROM cat_breeds WHERE name LIKE '%' || :searchQuery || '%'")
    suspend fun searchCatBreeds(searchQuery: String): List<CatBreedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatBreeds(catBreeds: List<CatBreedEntity>)

    @Update
    suspend fun updateCatBreed(catBreed: CatBreedEntity)

    @Delete
    suspend fun deleteCatBreed(catBreed: CatBreedEntity)

    @Query("SELECT * FROM cat_breeds")
    fun getAllCatBreedsFlow(): Flow<List<CatBreedEntity>>

    @Query("SELECT * FROM cat_breeds WHERE isFavorite = 1")
    fun getFavoriteCatBreedsFlow(): Flow<List<CatBreedEntity>>

    @Query("SELECT * FROM cat_breeds ORDER BY name ASC")
    fun pagingSource(): PagingSource<Int, CatBreedEntity>

    @Query("SELECT * FROM cat_breeds WHERE name LIKE :searchQuery || '%' ORDER BY name ASC")
    fun searchPagingSource(searchQuery: String): PagingSource<Int, CatBreedEntity>



}