package com.example.odpapp.data

import kotlinx.coroutines.flow.Flow

interface ResidenceRepository {

    suspend fun insertAll(residences: List<ResidenceEntity>)

    fun getAll(): Flow<List<ResidenceEntity>>

    fun getById(id: Int): Flow<ResidenceEntity?>

    suspend fun deleteAll()

    suspend fun getFirst(): ResidenceEntity?

}