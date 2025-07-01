package com.example.odpapp.ui.theme.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.odpapp.OdpApplication
import com.example.odpapp.data.FavoriteResidenceEntity
import com.example.odpapp.data.FavoriteVehicleEntity
import com.example.odpapp.data.FavoritesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

data class FavoritesUiState(
    val favoriteResidences: List<FavoriteResidenceEntity> = emptyList(),
    val favoriteVehicles: List<FavoriteVehicleEntity> = emptyList()
)

class FavoritesViewModel(favoritesRepository: FavoritesRepository) : ViewModel() {

    val favoriteResidences: StateFlow<List<FavoriteResidenceEntity>> =
        favoritesRepository.getAllFavoriteResidences()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

    val favoriteVehicles: StateFlow<List<FavoriteVehicleEntity>> =
        favoritesRepository.getAllFavoriteVehicles()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OdpApplication)
                FavoritesViewModel(
                    favoritesRepository = application.container.favoritesRepository
                )
            }
        }
    }
}