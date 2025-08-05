package com.example.catapp

import com.example.catapp.ui.screens.breedDetails.BreedDetailViewModel
import com.example.catapp.domain.model.CatBreed
import com.example.catapp.domain.repository.CatBreedRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import org.mockito.kotlin.argumentCaptor


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BreedDetailViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var repository: CatBreedRepository

    private lateinit var viewModel: BreedDetailViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val testBreed = CatBreed("1", "Persian", "desc", "Calm", "Iran", 15, false, "img")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init with breedId loads breed`() = runTest(testDispatcher) {
        val savedStateHandle = SavedStateHandle(mapOf("breedId" to "1"))
        `when`(repository.getCatBreedById("1")).thenReturn(testBreed)

        viewModel = BreedDetailViewModel(repository, savedStateHandle)

        advanceUntilIdle()

        Assert.assertEquals(testBreed, viewModel.breed.value)
    }

    @Test
    fun `init without breedId sets error`() = runTest {
        val savedStateHandle = SavedStateHandle()
        viewModel = BreedDetailViewModel(repository, savedStateHandle)

        Assert.assertEquals("Breed ID not found", viewModel.error.value)
    }

    @Test
    fun `toggleFavorite adds to favorites when not favorite`() = runTest(testDispatcher) {
        val savedStateHandle = SavedStateHandle(mapOf("breedId" to "1"))
        `when`(repository.getCatBreedById("1")).thenReturn(testBreed.copy(isFavorite = false))

        viewModel = BreedDetailViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        viewModel.toggleFavorite()
        advanceUntilIdle()

        val captor = argumentCaptor<CatBreed>()
        verify(repository).addCatBreedToFavorites(captor.capture())
        Assert.assertTrue(captor.firstValue.isFavorite.not())
    }

    @Test
    fun `toggleFavorite removes from favorites when already favorite`() = runTest(testDispatcher) {
        val savedStateHandle = SavedStateHandle(mapOf("breedId" to "1"))
        `when`(repository.getCatBreedById("1")).thenReturn(testBreed.copy(isFavorite = true))

        viewModel = BreedDetailViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        viewModel.toggleFavorite()
        advanceUntilIdle()

        val captor = argumentCaptor<CatBreed>()
        verify(repository).removeCatBreedFromFavorites(captor.capture())
        Assert.assertTrue(captor.firstValue.isFavorite)
    }
}