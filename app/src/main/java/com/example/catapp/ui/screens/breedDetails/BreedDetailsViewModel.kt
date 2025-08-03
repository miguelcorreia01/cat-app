package com.example.catapp.ui.screens.breedDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.domain.model.CatBreed
import com.example.catapp.domain.repository.CatBreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedDetailViewModel @Inject constructor(
    private val repository: CatBreedRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _breed = MutableStateFlow<CatBreed?>(null)
    val breed: StateFlow<CatBreed?> = _breed.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        val breedId = savedStateHandle.get<String>("breedId")
        if (breedId != null) {
            loadBreedDetail(breedId)
        } else {
            _error.value = "Breed ID not found"
        }
    }

    private fun loadBreedDetail(breedId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val breedData = repository.getCatBreedById(breedId)
                _breed.value = breedData
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load breed details"
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            try {
                val currentBreed = _breed.value
                if (currentBreed != null) {
                    if (currentBreed.isFavorite) {
                        repository.removeCatBreedFromFavorites(currentBreed)
                    } else {
                        repository.addCatBreedToFavorites(currentBreed)
                    }

                    _breed.value = currentBreed.copy(isFavorite = !currentBreed.isFavorite)
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update favorite status"
            }
        }
    }

}