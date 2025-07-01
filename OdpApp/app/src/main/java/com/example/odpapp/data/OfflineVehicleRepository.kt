package com.example.odpapp.data

import kotlinx.coroutines.flow.Flow

class OfflineVehicleRepository(private val vehicleDao: VehicleDao) : VehicleRepository{

    override suspend fun insertAll(vehicles: List<VehicleEntity>) = vehicleDao.insertAll(vehicles)

    override fun getAll(): Flow<List<VehicleEntity>> = vehicleDao.getAll()

    override fun getById(id: Int): Flow<VehicleEntity?> = vehicleDao.getById(id)

    override suspend fun deleteAll() = vehicleDao.deleteAll()

    override suspend fun getFirst(): VehicleEntity? = vehicleDao.getFirst()
}