package com.example.catapp

import com.example.catapp.domain.model.CatBreed
import com.example.catapp.domain.repository.CatBreedRepository
import com.example.catapp.ui.screens.favorites.FavoritesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoritesViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var repository: CatBreedRepository

    private lateinit var viewModel: FavoritesViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val breeds = listOf(
        CatBreed("1", "A", "desc", "Calm", "X", 12, true, "img"),
        CatBreed("2", "B", "desc", "Playful", "Y", 18, true, "img")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        `when`(repository.getFavoriteCatBreeds()).thenReturn(flowOf(breeds))
        viewModel = FavoritesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `calculateAverageLifeSpan returns correct average`() = runTest {
        val favoriteBreeds = listOf(
            CatBreed("1", "Persian", "desc", "Calm", "Iran", 15, true, "img"),
            CatBreed("2", "Siamese", "desc", "Social", "Thailand", 13, true, "img")
        )

        whenever(repository.getFavoriteCatBreeds()).thenReturn(flowOf(favoriteBreeds))

        val viewModel = FavoritesViewModel(repository)

        viewModel.favorites.first { it.isNotEmpty() }

        val avg = viewModel.calculateAverageLifeSpan()
        assertEquals(14.0, avg, 0.01)
    }

    @Test
    fun `removeFromFavorites calls repository`() = runTest(testDispatcher) {
        val breed = breeds[0]
        viewModel.removeFromFavorites(breed)
        advanceUntilIdle()

        verify(repository).removeCatBreedFromFavorites(breed)
    }
}