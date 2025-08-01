package com.example.catapp.data

import com.example.catapp.data.CatBreedMapper.toCatBreed
import com.example.catapp.data.CatBreedMapper.toCatBreedEntity
import com.example.catapp.data.CatBreedMapper.toCatBreedEntityList
import com.example.catapp.data.CatBreedMapper.toCatBreedList
import com.example.catapp.data.CatBreedMapper.toCatBreedListFromEntities
import com.example.catapp.data.api.CatApiService
import com.example.catapp.data.local.CatBreedDao
import com.example.catapp.domain.model.CatBreed
import com.example.catapp.domain.repository.CatBreedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CatBreedRepositoryImpl @Inject constructor(
    private val apiService: CatApiService,
    private val catBreedDao: CatBreedDao
) : CatBreedRepository {

    override suspend fun getCatBreeds(): List<CatBreed> {
        return try {
            val apiBreeds = apiService.getCatBreeds()
            val domainBreeds = apiBreeds.toCatBreedList()

            val entities = domainBreeds.toCatBreedEntityList()
            catBreedDao.insertCatBreeds(entities)

            domainBreeds
        } catch (e: Exception) {
            val localEntities = catBreedDao.getAllCatBreeds()
            localEntities.toCatBreedListFromEntities()
        }
    }

    override suspend fun getCatBreedById(id: String): CatBreed {
        return try {
            val entity = catBreedDao.getCatBreedById(id)
            entity.toCatBreed()
        } catch (e: Exception) {
            val apiBreeds = apiService.getCatBreeds()
            val domainBreeds = apiBreeds.toCatBreedList()
            val breed = domainBreeds.find { it.id == id }

            breed?.let {
                catBreedDao.insertCatBreeds(listOf(it.toCatBreedEntity()))
            }

            breed ?: throw Exception("Breed not found")
        }
    }

    override fun getFavoriteCatBreeds(): Flow<List<CatBreed>> {
        return catBreedDao.getFavoriteCatBreedsFlow()
            .map { entities -> entities.toCatBreedListFromEntities() }
    }

    override suspend fun addCatBreedToFavorites(catBreed: CatBreed) {
        val entity = catBreed.toCatBreedEntity()
        entity.isFavorite = true
        catBreedDao.updateCatBreed(entity)
    }

    override suspend fun removeCatBreedFromFavorites(catBreed: CatBreed) {
        val entity = catBreed.toCatBreedEntity()
        entity.isFavorite = false
        catBreedDao.updateCatBreed(entity)
    }

    override suspend fun searchCatBreeds(query: String): List<CatBreed> {
        return catBreedDao.searchCatBreeds(query).toCatBreedListFromEntities()
    }

    override suspend fun refreshCatBreeds() {
        try {
            val apiBreeds = apiService.getCatBreeds()
            val domainBreeds = apiBreeds.toCatBreedList()
            val entities = domainBreeds.toCatBreedEntityList()
            catBreedDao.insertCatBreeds(entities)
        } catch (e: Exception) {
            throw e
        }
    }
}