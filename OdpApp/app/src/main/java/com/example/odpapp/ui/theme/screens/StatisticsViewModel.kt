package com.example.odpapp.ui.theme.screens

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.odpapp.OdpApplication
import com.example.odpapp.data.ResidenceEntity
import com.example.odpapp.data.ResidenceRepository
import com.example.odpapp.data.VehicleEntity
import com.example.odpapp.data.VehicleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// Data klase za obrađene podatke za grafikone
data class ChartData(val label: String, val value: Float, val color: Color)

data class StatisticsUiState(
    val populationByEntity: List<ChartData> = emptyList(),
    val vehicleTypeDistribution: List<ChartData> = emptyList(),
    val isLoading: Boolean = true
)

class StatisticsViewModel(
    residenceRepository: ResidenceRepository,
    vehicleRepository: VehicleRepository
) : ViewModel() {

    // Korištenje 'combine' za ispravno spajanje dva flow-a
    val uiState: StateFlow<StatisticsUiState> =
        combine(
            residenceRepository.getAll(),
            vehicleRepository.getAll()
        ) { residences, vehicles ->
            // Ovaj blok se izvršava kada bilo koji od flow-ova emitira novu vrijednost
            StatisticsUiState(
                populationByEntity = processPopulationData(residences),
                vehicleTypeDistribution = processVehicleData(vehicles),
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = StatisticsUiState(isLoading = true)
        )

    private fun processPopulationData(residences: List<ResidenceEntity>): List<ChartData> {
        if (residences.isEmpty()) return emptyList()
        return residences
            .groupBy { it.entitet }
            .map { (entity, residenceList) ->
                ChartData(
                    label = entity,
                    value = residenceList.sumOf { it.total }.toFloat(),
                    color = getRandomColor()
                )
            }
    }

    private fun processVehicleData(vehicles: List<VehicleEntity>): List<ChartData> {
        if (vehicles.isEmpty()) return emptyList()
        val totalDomestic = vehicles.sumOf { it.domVoz }.toFloat()
        val totalForeign = vehicles.sumOf { it.strVoz }.toFloat()
        return listOf(
            ChartData("Domaća", totalDomestic, Color(0xFF4CAF50)),
            ChartData("Strana", totalForeign, Color(0xFFF44336))
        )
    }

    private fun getRandomColor(): Color {
        val r = (30..200).random()
        val g = (30..200).random()
        val b = (30..200).random()
        return Color(r, g, b)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OdpApplication)
                StatisticsViewModel(
                    residenceRepository = application.container.residenceRepository,
                    vehicleRepository = application.container.vehicleRepository
                )
            }
        }
    }
}