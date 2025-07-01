package com.example.odpapp.data

import com.example.odpapp.model.Dataset
import com.example.odpapp.model.ResidenceData
import com.example.odpapp.model.ResidenceRequest
import com.example.odpapp.model.VehicleData
import com.example.odpapp.model.VehicleRequest
import com.example.odpapp.network.ApiService
import kotlinx.coroutines.flow.Flow

interface OdpRepository {
    suspend fun getDatasets(): List<Dataset>
    suspend fun getResidenceData(url: String, body: ResidenceRequest): List<ResidenceData>
    suspend fun getVehiclesData(url: String, body: VehicleRequest): List<VehicleData>
    fun getResidenceById(id: Int): Flow<ResidenceEntity?>
    fun getVehicleById(id: Int): Flow<VehicleEntity?>
}

class NetworkOdpRepository(
    private val apiService: ApiService,
    private val residenceRepository: ResidenceRepository,
    private val vehicleRepository: VehicleRepository
) : OdpRepository {

    override suspend fun getDatasets(): List<Dataset> = apiService.getDatasets().result

    override suspend fun getResidenceData(url: String, body: ResidenceRequest): List<ResidenceData> {
        println("🌐 Šaljem zahtev za prebivališta na: $url")
        val response = apiService.getResidenceData(url, body)

        // Proveri da li su podaci već u bazi
        val isDbEmpty = residenceRepository.getFirst() == null
        if (isDbEmpty) {
            val entities = response.result.map {
                ResidenceEntity(
                    entitet = it.entity,
                    canton = it.canton,
                    municipality = it.municipality,
                    institution = it.institution ?: "",
                    dateUpdate = it.dateUpdate,
                    saTrajPreb = it.withPermanentResidenceTotal,
                    saPrivPreb = it.withTemporaryResidenceTotal,
                    total = it.total
                )
            }
            residenceRepository.insertAll(entities)
        }

        println("📥 Odgovor: ${response.result.size} stavki")
        return response.result
    }


    override suspend fun getVehiclesData(url: String, body: VehicleRequest): List<VehicleData> {
        println("🌐 Šaljem zahtev za vozila na: $url")
        val response = apiService.getVehiclesData(url, body)

        // Proveri da li su podaci već u bazi
        val isDbEmpty = vehicleRepository.getFirst() == null
        if (isDbEmpty) {
            val entities = response.result.map {
                VehicleEntity(
                    entity = it.entity,
                    canton = it.canton,
                    municipality = it.municipality,
                    year = it.year,
                    month = it.month,
                    dateUpdate = it.dateUpdate,
                    mjestoReg = it.registrationPlace,
                    domVoz = it.totalDomestic,
                    strVoz = it.totalForeign,
                    total = it.total
                )
            }
            vehicleRepository.insertAll(entities)
        }

        println("📥 Odgovor: ${response.result.size} vozila")
        return response.result
    }

    override fun getResidenceById(id: Int): Flow<ResidenceEntity?> {
        return residenceRepository.getById(id)
    }

    override fun getVehicleById(id: Int): Flow<VehicleEntity?> {
        return vehicleRepository.getById(id)
    }
}