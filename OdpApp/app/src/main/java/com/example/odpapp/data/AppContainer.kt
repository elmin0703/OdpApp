package com.example.odpapp.data

import android.content.Context
import com.example.odpapp.network.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppContainer {
    val odpRepository: OdpRepository
    val residenceRepository: ResidenceRepository
    val vehicleRepository: VehicleRepository
    val favoritesRepository: FavoritesRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val baseUrl = "https://odp.iddeea.gov.ba:8096/"
    private val token = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMDc0IiwibmJmIjoxNzUxMzM2NTIyLCJleHAiOjE3NTE0MjI5MjIsImlhdCI6MTc1MTMzNjUyMn0.3rWAE_Pt89raVC_gUnetqOAJEY5GhghWGT6PkihDirtmS_dl6lwFW2o5bgTiLDSUiPoayAv2vvgZc1k10kxPGQ"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", token)
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(newRequest)
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .client(client)
        .build()

    private val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    override val odpRepository: OdpRepository by lazy {
        NetworkOdpRepository(retrofitService, residenceRepository, vehicleRepository)
    }

    override val residenceRepository: ResidenceRepository by lazy {
        OfflineResidenceRepository(database.residenceDao())
    }

    override val vehicleRepository: VehicleRepository by lazy {
        OfflineVehicleRepository(database.vehicleDao())
    }

    override val favoritesRepository: FavoritesRepository by lazy {
        OfflineFavoritesRepository(database.favoriteResidenceDao(), database.favoriteVehicleDao())
    }
}