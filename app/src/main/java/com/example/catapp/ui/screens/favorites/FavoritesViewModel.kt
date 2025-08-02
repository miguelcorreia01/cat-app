package com.example.catapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapp.domain.model.CatBreed
import com.example.catapp.domain.repository.CatBreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
  private val repository: CatBreedRepository
) : ViewModel() {

    val favorites = repository.getFavoriteCatBreeds()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun removeFromFavorites(breed: CatBreed) {
        viewModelScope.launch {
            repository.removeCatBreedFromFavorites(breed)
        }
    }

    fun calculateAverageLifeSpan(): Double {
        return favorites.value.map { it.lifeSpan }.average()
    }
}


