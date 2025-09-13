package com.example.odpapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteVehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteVehicleEntity)

    @Query("DELETE FROM favorite_residence WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM favorite_vehicles")
    fun getAll(): Flow<List<FavoriteVehicleEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_vehicles WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>

    @Query("SELECT * FROM favorite_vehicles WHERE id = :id")
    fun getById(id: Int): Flow<FavoriteVehicleEntity?>
}