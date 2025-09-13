package com.example.odpapp.ui.theme.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.odpapp.OdpApplication
import com.example.odpapp.data.OdpRepository
import com.example.odpapp.data.ResidenceRepository
import com.example.odpapp.data.VehicleRepository
import com.example.odpapp.model.ResidenceRequest
import com.example.odpapp.model.VehicleRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface DatasetDetailUiState {
    data object Loading : DatasetDetailUiState
    data class Success(val data: List<Any>) : DatasetDetailUiState
    data class Error(val message: String) : DatasetDetailUiState
}

class DatasetDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val odpRepository: OdpRepository,
    private val residenceRepository: ResidenceRepository,
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val datasetId: String = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow<DatasetDetailUiState>(DatasetDetailUiState.Loading)
    val uiState: StateFlow<DatasetDetailUiState> = _uiState.asStateFlow()

    init {
        loadDatasetDetails()
    }

    fun loadDatasetDetails() {
        viewModelScope.launch {
            _uiState.value = DatasetDetailUiState.Loading
            try {
                val dataset = odpRepository.getDatasets()
                    .firstOrNull { it.id.toString() == datasetId }
                    ?: throw Exception("Dataset $datasetId nije pronađen")

                println("🔍 Učitavam podatke za: ${dataset.name} (${dataset.apiUrl})")

                when (dataset.id) {
                    7 -> {
                        val request = ResidenceRequest("2025-06-03", 0, 0, 0)
                        odpRepository.getResidenceData(dataset.apiUrl, request)
                        val localData = residenceRepository.getAll().first()
                        _uiState.value = DatasetDetailUiState.Success(localData)
                    }
                    19 -> {
                        val request = VehicleRequest("2025-06-03", 0, 0, 0, "", "")
                        odpRepository.getVehiclesData(dataset.apiUrl, request)
                        val localData = vehicleRepository.getAll().first()
                        _uiState.value = DatasetDetailUiState.Success(localData)
                    }
                }
            } catch (e: Exception) {
                println("❌ Greška u ViewModel: ${e.message}")
                _uiState.value = DatasetDetailUiState.Error(e.message ?: "Greška pri učitavanju")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OdpApplication)
                DatasetDetailViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    odpRepository = application.container.odpRepository,
                    residenceRepository = application.container.residenceRepository,
                    vehicleRepository = application.container.vehicleRepository
                )
            }
        }
    }
}