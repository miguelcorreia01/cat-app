package com.example.catapp.ui.screens.breedList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.domain.model.CatBreed
import com.example.catapp.domain.model.CatBreedsUiState
import com.example.catapp.domain.repository.CatBreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val repository: CatBreedRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatBreedsUiState())
    val uiState: StateFlow<CatBreedsUiState> = _uiState.asStateFlow()

    init {
        loadCatBreeds()
    }

    private fun loadCatBreeds() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val breeds = repository.getCatBreeds()
                _uiState.update {
                    it.copy(
                        breeds = breeds,
                        filteredBreeds = breeds,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error loading breeds"
                    )
                }
            }
        }
    }

    fun searchBreeds(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        if (query.isBlank()) {
            _uiState.update { it.copy(filteredBreeds = it.breeds) }
            return
        }

        viewModelScope.launch {
            try {
                val searchResults = repository.searchCatBreeds(query)
                _uiState.update { it.copy(filteredBreeds = searchResults) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Search failed"
                    )
                }
            }
        }
    }

    fun toggleFavorite(breed: CatBreed) {
        viewModelScope.launch {
            try {
                if (breed.isFavorite) {
                    repository.removeCatBreedFromFavorites(breed)
                } else {
                    repository.addCatBreedToFavorites(breed)
                }
                updateBreedInList(breed.copy(isFavorite = !breed.isFavorite))

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Failed to update favorite"
                    )
                }
            }
        }
    }

    private fun updateBreedInList(updatedBreed: CatBreed) {
        _uiState.update { currentState ->
            val updatedBreeds = currentState.breeds.map { breed ->
                if (breed.id == updatedBreed.id) updatedBreed else breed
            }

            val updatedFilteredBreeds = currentState.filteredBreeds.map { breed ->
                if (breed.id == updatedBreed.id) updatedBreed else breed
            }
            currentState.copy(
                breeds = updatedBreeds,
                filteredBreeds = updatedFilteredBreeds
            )
        }
    }
}
