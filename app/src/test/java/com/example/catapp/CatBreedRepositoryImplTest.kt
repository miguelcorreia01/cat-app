package com.example.catapp


import com.example.catapp.data.CatBreedRepositoryImpl
import com.example.catapp.data.api.CatApiService
import com.example.catapp.data.api.CatBreedDto
import com.example.catapp.data.local.CatBreedDao
import com.example.catapp.data.local.CatBreedEntity
import com.example.catapp.domain.model.CatBreed
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class CatBreedRepositoryImplTest {

    @Mock private lateinit var apiService: CatApiService
    @Mock private lateinit var catBreedDao: CatBreedDao

    private lateinit var repository: CatBreedRepositoryImpl

    @Before
    fun setup() {
        repository = CatBreedRepositoryImpl(apiService, catBreedDao)
    }

    @Test
    fun `getCatBreeds returns local data when available`() = runTest {
        val localEntities = listOf(
            CatBreedEntity(
                id = "test-id",
                name = "Test Breed",
                description = "Test description",
                temperament = "Friendly",
                origin = "Test Origin",
                lifeSpan = 15,
                isFavorite = false,
                image = "test-image-url"
            )
        )
        whenever(catBreedDao.getAllCatBreeds()).thenReturn(localEntities)

        val result = repository.getCatBreeds()

        assertEquals(1, result.size)
        assertEquals("Test Breed", result[0].name)
        verify(apiService, never()).getCatBreeds()
    }

    @Test
    fun `getCatBreeds fetches from API when local is empty`() = runTest {
        val apiBreeds = listOf(
            CatBreedDto(
                id = "api-id",
                name = "API Breed",
                description = "API description",
                temperament = "Active",
                origin = "API Origin",
                lifeSpan = "10 - 15",
                referenceImageId = "img123"
            )
        )
        whenever(catBreedDao.getAllCatBreeds()).thenReturn(emptyList())
        whenever(apiService.getCatBreeds()).thenReturn(apiBreeds)

        val result = repository.getCatBreeds()

        assertEquals(1, result.size)
        assertEquals("API Breed", result[0].name)
        verify(apiService).getCatBreeds()
        verify(catBreedDao).insertCatBreeds(any())
    }

    @Test
    fun `getCatBreedById returns local breed when found`() = runTest {
        val entity = CatBreedEntity(
            id = "test-id",
            name = "Test Breed",
            description = "Test description",
            temperament = "Friendly",
            origin = "Test Origin",
            lifeSpan = 15,
            isFavorite = false,
            image = "test-image-url"
        )
        whenever(catBreedDao.getCatBreedById("test-id")).thenReturn(entity)

        val result = repository.getCatBreedById("test-id")

        assertEquals("Test Breed", result.name)
        assertEquals("test-id", result.id)
    }

    @Test
    fun `getCatBreedById fetches from API when not found locally`() = runTest {
        val apiBreeds = listOf(
            CatBreedDto(
                id = "api-id",
                name = "API Breed",
                description = "API description",
                temperament = "Active",
                origin = "API Origin",
                lifeSpan = "10 - 15",
                referenceImageId = "img123"
            )
        )
        whenever(catBreedDao.getCatBreedById("api-id")).thenThrow(RuntimeException("Not found"))
        whenever(apiService.getCatBreeds()).thenReturn(apiBreeds)

        val result = repository.getCatBreedById("api-id")

        assertEquals("API Breed", result.name)
        verify(apiService).getCatBreeds()
        verify(catBreedDao).insertCatBreeds(any())
    }

    @Test
    fun `searchCatBreeds returns filtered results`() = runTest {
        val searchQuery = "Persian"
        val searchResults = listOf(
            CatBreedEntity(
                id = "persian-id",
                name = "Persian",
                description = "Persian description",
                temperament = "Calm",
                origin = "Persia",
                lifeSpan = 12,
                isFavorite = false,
                image = null
            )
        )
        whenever(catBreedDao.searchCatBreeds(searchQuery)).thenReturn(searchResults)

        val result = repository.searchCatBreeds(searchQuery)

        assertEquals(1, result.size)
        assertEquals("Persian", result[0].name)
        verify(catBreedDao).searchCatBreeds(searchQuery)
    }

    @Test
    fun `addCatBreedToFavorites calls updateCatBreed`() = runTest {
        val breed = CatBreed(
            id = "test-id",
            name = "Test Breed",
            description = "Test description",
            temperament = "Friendly",
            origin = "Test Origin",
            lifeSpan = 15,
            isFavorite = false,
            image = "test-image-url"
        )

        repository.addCatBreedToFavorites(breed)

        verify(catBreedDao).updateCatBreed(any())
    }

    @Test
    fun `removeCatBreedFromFavorites calls updateCatBreed`() = runTest {
        val breed = CatBreed(
            id = "test-id",
            name = "Test Breed",
            description = "Test description",
            temperament = "Friendly",
            origin = "Test Origin",
            lifeSpan = 15,
            isFavorite = true,
            image = "test-image-url"
        )

        repository.removeCatBreedFromFavorites(breed)

        verify(catBreedDao).updateCatBreed(any())
    }

    @Test
    fun `refreshCatBreeds fetches from API and preserves favorites`() = runTest {
        val apiBreeds = listOf(
            CatBreedDto(
                id = "api-id",
                name = "API Breed",
                description = "API description",
                temperament = "Active",
                origin = "API Origin",
                lifeSpan = "10 - 15",
                referenceImageId = "img123"
            )
        )
        val currentFavorites = listOf(
            CatBreedEntity(
                id = "api-id",
                name = "API Breed",
                description = "API description",
                temperament = "Active",
                origin = "API Origin",
                lifeSpan = 15,
                isFavorite = true,
                image = "test-image-url"
            )
        )
        whenever(apiService.getCatBreeds()).thenReturn(apiBreeds)
        whenever(catBreedDao.getFavoriteCatBreeds()).thenReturn(currentFavorites)

        repository.refreshCatBreeds()

        verify(apiService).getCatBreeds()
        verify(catBreedDao).getFavoriteCatBreeds()
        verify(catBreedDao).insertCatBreeds(any())
    }

    @Test
    fun `getFavoriteCatBreeds returns flow`() = runTest {
        whenever(catBreedDao.getFavoriteCatBreedsFlow()).thenReturn(flowOf(emptyList()))

        repository.getFavoriteCatBreeds()

        verify(catBreedDao).getFavoriteCatBreedsFlow()
    }
}