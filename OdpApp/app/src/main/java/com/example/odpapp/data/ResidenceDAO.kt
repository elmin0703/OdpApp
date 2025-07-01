package com.example.odpapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ResidenceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(residences: List<ResidenceEntity>)

    @Query("SELECT * FROM residence")
    fun getAll(): Flow<List<ResidenceEntity>>

    @Query("SELECT * FROM residence WHERE id = :id")
    fun getById(id: Int): Flow<ResidenceEntity?>

    @Query("DELETE FROM residence")
    suspend fun deleteAll()

    @Query("SELECT * FROM residence LIMIT 1")
    suspend fun getFirst(): ResidenceEntity?

}