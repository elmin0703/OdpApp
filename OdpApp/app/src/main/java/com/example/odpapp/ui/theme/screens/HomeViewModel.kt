package com.example.odpapp.ui.theme.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.odpapp.OdpApplication
import com.example.odpapp.data.OdpRepository
import com.example.odpapp.model.Dataset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed interface HomeUiState {
    data object Loading: HomeUiState
    data class Success(val data: List<Dataset>) : HomeUiState
    data object Error: HomeUiState
}

class HomeViewModel(private val odpRepository: OdpRepository) : ViewModel() {
    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()


    init {
        loadDatasets()
    }

    fun loadDatasets() {
        viewModelScope.launch {
            _homeUiState.value = HomeUiState.Loading // Ažuriranje vrednosti
            _homeUiState.value = try {
                val response = odpRepository.getDatasets()
                println("✅ API odgovor: ${response.size} datasetova")

                val filtered = response.filter {
                    it.id == 7 || it.id == 19
                }

                HomeUiState.Success(filtered)
            } catch (e: Exception) {
                println("❌ Greška: ${e.message}")
                HomeUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OdpApplication)
                val odpRepository = application.container.odpRepository
                HomeViewModel(odpRepository = odpRepository)
            }
        }
    }
}

