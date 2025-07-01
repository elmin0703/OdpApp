package com.example.odpapp.network

import com.example.odpapp.model.ApiResponse
import com.example.odpapp.model.ResidenceApiResponse
import com.example.odpapp.model.ResidenceData
import com.example.odpapp.model.ResidenceRequest
import com.example.odpapp.model.VehicleApiResponse
import com.example.odpapp.model.VehicleData
import com.example.odpapp.model.VehicleRequest
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @Headers("Accept: application/json")
    @POST("api/dataset/list")
    suspend fun getDatasets(@Body language: LanguageRequest = LanguageRequest(languageId = 1)): ApiResponse

    // API poziv za dohvatanje podataka o prebivalištu
    @Headers("Accept: application/json")
    @POST
    suspend fun getResidenceData(@Url url: String, @Body body: ResidenceRequest): ResidenceApiResponse

    // API poziv za dohvatanje podataka o vozilima
    @Headers("Accept: application/json")
    @POST
    suspend fun getVehiclesData(@Url url: String, @Body body: VehicleRequest): VehicleApiResponse
}

@Serializable
data class LanguageRequest(val languageId: Int)