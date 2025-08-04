package com.example.catapp.ui.screens.breedList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.domain.model.CatBreed
import com.example.catapp.domain.model.CatBreedsUiState
import com.example.catapp.domain.repository.CatBreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val repository: CatBreedRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatBreedsUiState())
    val uiState: StateFlow<CatBreedsUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val breeds = repository.getAllCatBreedsFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val filteredBreeds = combine(breeds, searchQuery) { breedsList, query ->
        if (query.isBlank()) {
            breedsList
        } else {
            breedsList.filter { breed ->
                breed.name.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadCatBreedsIfNeeded()
    }

    private fun loadCatBreedsIfNeeded() {
        viewModelScope.launch {
            val currentBreeds = breeds.value
            if (currentBreeds.isEmpty()) {
                loadCatBreeds()
            }
        }
    }

    private fun loadCatBreeds() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                repository.getCatBreeds()
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
                _isLoading.value = false
            }
        }
    }


    fun searchBreeds(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(breed: CatBreed) {
        viewModelScope.launch {
            try {
                if (breed.isFavorite) {
                    repository.removeCatBreedFromFavorites(breed)
                } else {
                    repository.addCatBreedToFavorites(breed)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Failed to update favorite"
                    )
                }
            }
        }
    }

}
