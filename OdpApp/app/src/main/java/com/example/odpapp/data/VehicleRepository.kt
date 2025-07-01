package com.example.odpapp.data

import kotlinx.coroutines.flow.Flow

interface VehicleRepository {

    suspend fun insertAll(vehicles: List<VehicleEntity>)

    fun getAll(): Flow<List<VehicleEntity>>

    fun getById(id: Int): Flow<VehicleEntity?>

    suspend fun deleteAll()

    suspend fun getFirst(): VehicleEntity?
}