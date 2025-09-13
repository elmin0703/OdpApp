package com.example.odpapp.data

import kotlinx.coroutines.flow.Flow

class OfflineResidenceRepository(private val residenceDao: ResidenceDao) : ResidenceRepository {
    override fun getAll(): Flow<List<ResidenceEntity>> = residenceDao.getAll()

    override fun getById(id: Int): Flow<ResidenceEntity?> = residenceDao.getById(id)

    override suspend fun insertAll(residences: List<ResidenceEntity>) = residenceDao.insertAll(residences)

    override suspend fun deleteAll() = residenceDao.deleteAll()

    override suspend fun getFirst(): ResidenceEntity? = residenceDao.getFirst()
}