package com.example.odpapp.data

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getAllFavoriteResidences(): Flow<List<FavoriteResidenceEntity>>
    fun getAllFavoriteVehicles(): Flow<List<FavoriteVehicleEntity>>
    fun isResidenceFavorite(id: Int): Flow<Boolean>
    fun isVehicleFavorite(id: Int): Flow<Boolean>
    suspend fun addResidenceToFavorites(residence: ResidenceEntity)
    suspend fun removeResidenceFromFavorites(id: Int)
    suspend fun addVehicleToFavorites(vehicle: VehicleEntity)
    suspend fun removeVehicleFromFavorites(id: Int)
    fun getFavoriteResidenceById(id: Int): Flow<FavoriteResidenceEntity?>
    fun getFavoriteVehicleById(id: Int): Flow<FavoriteVehicleEntity?>
}