package com.example.catapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.catapp.data.local.CatBreedDao
import com.example.catapp.data.local.CatBreedDatabase
import com.example.catapp.data.local.CatBreedEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CatBreedDaoTest {

    private lateinit var database: CatBreedDatabase
    private lateinit var dao: CatBreedDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CatBreedDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.catBreedDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetAllCatBreeds_returnsInsertedBreeds() = runBlocking {
        val breed = CatBreedEntity("1", "Persian", "desc", "Calm", "Iran", 15, false, "img")
        dao.insertCatBreeds(listOf(breed))

        val allBreeds = dao.getAllCatBreeds()
        assertEquals(1, allBreeds.size)
        assertEquals("Persian", allBreeds[0].name)
    }

    @Test
    fun getCatBreedById_returnsCorrectBreed() = runBlocking {
        val breed = CatBreedEntity("2", "Siamese", "desc", "Social", "Thailand", 12, false, "img")
        dao.insertCatBreeds(listOf(breed))

        val found = dao.getCatBreedById("2")
        assertNotNull(found)
        assertEquals("Siamese", found.name)
    }

    @Test
    fun updateCatBreed_changesData() = runBlocking {
        val breed = CatBreedEntity("3", "Maine Coon", "desc", "Friendly", "USA", 14, false, "img")
        dao.insertCatBreeds(listOf(breed))

        val updatedBreed = breed.copy(name = "Maine Coon Updated")
        dao.updateCatBreed(updatedBreed)

        val fetched = dao.getCatBreedById("3")
        assertEquals("Maine Coon Updated", fetched.name)
    }

    @Test
    fun deleteCatBreed_removesBreed() = runBlocking {
        val breed = CatBreedEntity("4", "Bengal", "desc", "Energetic", "USA", 13, false, "img")
        dao.insertCatBreeds(listOf(breed))

        dao.deleteCatBreed(breed)

        val allBreeds = dao.getAllCatBreeds()
        assertEquals(0, allBreeds.size)
    }

    @Test
    fun getFavoriteCatBreeds_returnsOnlyFavorites() = runBlocking {
        val favBreed = CatBreedEntity("5", "Siberian", "desc", "Calm", "Russia", 15, true, "img")
        val nonFavBreed = CatBreedEntity("6", "Scottish Fold", "desc", "Quiet", "Scotland", 12, false, "img")
        dao.insertCatBreeds(listOf(favBreed, nonFavBreed))

        val favorites = dao.getFavoriteCatBreeds()
        assertEquals(1, favorites.size)
        assertEquals("Siberian", favorites[0].name)
    }

    @Test
    fun searchCatBreeds_findsByName() = runBlocking {
        val breed1 = CatBreedEntity("7", "Ragdoll", "desc", "Gentle", "USA", 14, false, "img")
        val breed2 = CatBreedEntity("8", "Russian Blue", "desc", "Quiet", "Russia", 12, false, "img")
        dao.insertCatBreeds(listOf(breed1, breed2))

        val results = dao.searchCatBreeds("Russ")
        assertEquals(1, results.size)
        assertEquals("Russian Blue", results[0].name)
    }

    @Test
    fun getAllCatBreedsFlow_emitsData() = runBlocking {
        val breed = CatBreedEntity("9", "Birman", "desc", "Playful", "Myanmar", 15, false, "img")
        dao.insertCatBreeds(listOf(breed))

        val flowList = dao.getAllCatBreedsFlow().first()
        assertEquals(1, flowList.size)
        assertEquals("Birman", flowList[0].name)
    }

    @Test
    fun getFavoriteCatBreedsFlow_emitsFavorites() = runBlocking {
        val favBreed = CatBreedEntity("10", "Abyssinian", "desc", "Active", "Ethiopia", 14, true, "img")
        val nonFavBreed = CatBreedEntity("11", "Exotic Shorthair", "desc", "Calm", "USA", 13, false, "img")
        dao.insertCatBreeds(listOf(favBreed, nonFavBreed))

        val favoritesFlow = dao.getFavoriteCatBreedsFlow().first()
        assertEquals(1, favoritesFlow.size)
        assertEquals("Abyssinian", favoritesFlow[0].name)
    }
}