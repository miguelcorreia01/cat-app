package com.example.catapp.ui.screens.breedList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.catapp.domain.model.CatBreed
import com.example.catapp.domain.repository.CatBreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val repository: CatBreedRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val breedsPaging = _searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.getCatBreedsPaging()
            } else {
                repository.getCatBreedsPagingWithSearch(query)
            }
        }
        .cachedIn(viewModelScope)

    init {
        loadInitialDataIfNeeded()
    }

    private fun loadInitialDataIfNeeded() {
        viewModelScope.launch {
            try {
                val hasData = repository.getAllCatBreedsFlow().first().isNotEmpty()
                if (!hasData) {
                    loadInitialData()
                }
            } catch (e: Exception) {
                loadInitialData()
            }
        }
    }

    private fun loadInitialData() {
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
                _error.value = e.message ?: "Failed to update favorite"
            }
        }
    }

}

