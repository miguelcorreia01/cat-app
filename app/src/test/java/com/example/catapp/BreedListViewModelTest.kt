package com.example.catapp

import com.example.catapp.domain.model.CatBreed
import com.example.catapp.domain.repository.CatBreedRepository
import com.example.catapp.ui.screens.breedList.BreedListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class BreedListViewModelTest {

    @Mock private lateinit var repository: CatBreedRepository
    private lateinit var viewModel: BreedListViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val testBreeds = listOf(
        CatBreed("1", "Persian", "desc", "Calm", "Iran", 15, false, "img"),
        CatBreed("2", "Siamese", "desc", "Social", "Thailand", 13, false, "img"),
        CatBreed("3", "Maine Coon", "desc", "Gentle", "USA", 12, false, "img")
    )

    @Before
    fun setup() = runTest {
        Dispatchers.setMain(testDispatcher)
        whenever(repository.getAllCatBreedsFlow()).thenReturn(flowOf(testBreeds))
        viewModel = BreedListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial data loading when no data exists`() = runTest {
        reset(repository)

        whenever(repository.getAllCatBreedsFlow()).thenReturn(flowOf(emptyList()))
        whenever(repository.getCatBreeds()).thenReturn(testBreeds)

        viewModel = BreedListViewModel(repository)
        advanceUntilIdle()

        verify(repository).getCatBreeds()
    }

    @Test
    fun `initial data loading when data exists`() = runTest {
        reset(repository)

        whenever(repository.getAllCatBreedsFlow()).thenReturn(flowOf(testBreeds))

        viewModel = BreedListViewModel(repository)
        advanceUntilIdle()

        verify(repository, never()).getCatBreeds()
    }


    @Test
    fun `searchBreeds updates search query`() = runTest {
        viewModel.searchBreeds("Persian")
        advanceUntilIdle()

        val searchQuery = viewModel.searchQuery.first()
        assertEquals("Persian", searchQuery)
    }

    @Test
    fun `searchBreeds with empty query clears search`() = runTest {
        viewModel.searchBreeds("Persian")
        advanceUntilIdle()

        viewModel.searchBreeds("")
        advanceUntilIdle()

        val searchQuery = viewModel.searchQuery.first()
        assertEquals("", searchQuery)
    }



    @Test
    fun `toggleFavorite when not favorite`() = runTest {
        val breed = testBreeds[0].copy(isFavorite = false)

        viewModel.toggleFavorite(breed)
        advanceUntilIdle()

        verify(repository).addCatBreedToFavorites(breed)
    }

    @Test
    fun `toggleFavorite when already favorite`() = runTest {
        val breed = testBreeds[0].copy(isFavorite = true)

        viewModel.toggleFavorite(breed)
        advanceUntilIdle()

        verify(repository).removeCatBreedFromFavorites(breed)
    }

}