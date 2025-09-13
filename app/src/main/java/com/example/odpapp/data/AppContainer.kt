package com.example.odpapp.data

import android.content.Context
import com.example.odpapp.network.ApiService
import com.example.odpapp.network.AuthHeaderInterceptor
import com.example.odpapp.network.TokenExpiryGuardInterceptor
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

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthHeaderInterceptor(context.applicationContext))
            .addInterceptor(TokenExpiryGuardInterceptor(context.applicationContext))
            .build()
    }

    private val retrofit: Retrofit by lazy {
        val json = Json { ignoreUnknownKeys = true; explicitNulls = false }
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
            .client(client)
            .build()
    }

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
