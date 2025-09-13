package com.example.odpapp.ui.theme.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.odpapp.OdpApplication
import com.example.odpapp.data.FavoritesRepository
import com.example.odpapp.data.OdpRepository
import com.example.odpapp.data.ResidenceEntity
import com.example.odpapp.data.VehicleEntity
import com.example.odpapp.data.toResidenceEntity
import com.example.odpapp.data.toVehicleEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull

sealed interface RowDetailUiState {
    data class Success(val data: Any) : RowDetailUiState
    data object Error : RowDetailUiState
    data object Loading : RowDetailUiState
}


class DatasetRowDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val odpRepository: OdpRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RowDetailUiState>(RowDetailUiState.Loading)
    val uiState: StateFlow<RowDetailUiState> = _uiState.asStateFlow()

    // State za praćenje statusa favorita
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val entityType: String = checkNotNull(savedStateHandle["entityType"])
    private val entityId: Int = checkNotNull(savedStateHandle["entityId"])

    private var currentEntity: Any? = null

    init {
        fetchEntityDetails()
        observeFavoriteStatus()
    }

    private fun fetchEntityDetails() {
        viewModelScope.launch {
            _uiState.value = RowDetailUiState.Loading
            try {
                val data: Any? = if (entityType == "residence") {
                    odpRepository.getResidenceById(entityId).firstOrNull()
                        ?: favoritesRepository.getFavoriteResidenceById(entityId).firstOrNull()?.toResidenceEntity()
                } else { // "vehicles"
                    odpRepository.getVehicleById(entityId).firstOrNull()
                        ?: favoritesRepository.getFavoriteVehicleById(entityId).firstOrNull()?.toVehicleEntity()
                }

                if (data != null) {
                    currentEntity = data
                    _uiState.value = RowDetailUiState.Success(data)
                } else {
                    _uiState.value = RowDetailUiState.Error
                }

            } catch (e: Exception) {
                _uiState.value = RowDetailUiState.Error
            }
        }
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            when (entityType) {
                "residence" -> favoritesRepository.isResidenceFavorite(entityId).collectLatest { _isFavorite.value = it }
                "vehicles" -> favoritesRepository.isVehicleFavorite(entityId).collectLatest { _isFavorite.value = it }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val entity = currentEntity ?: return@launch
            if (_isFavorite.value) {
                // Ukloni iz favorita
                when (entity) {
                    is ResidenceEntity -> favoritesRepository.removeResidenceFromFavorites(entity.id)
                    is VehicleEntity -> favoritesRepository.removeVehicleFromFavorites(entity.id)
                }
            } else {
                // Dodaj u favorite
                when (entity) {
                    is ResidenceEntity -> favoritesRepository.addResidenceToFavorites(entity)
                    is VehicleEntity -> favoritesRepository.addVehicleToFavorites(entity)
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OdpApplication)
                DatasetRowDetailViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    odpRepository = application.container.odpRepository,
                    favoritesRepository = application.container.favoritesRepository // Dodaj
                )
            }
        }
    }
}