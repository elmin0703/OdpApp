package com.example.odpapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteResidenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteResidenceEntity)

    @Query("DELETE FROM favorite_residence WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM favorite_residence")
    fun getAll(): Flow<List<FavoriteResidenceEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_residence WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>

    @Query("SELECT * FROM favorite_residence WHERE id = :id")
    fun getById(id: Int): Flow<FavoriteResidenceEntity?>
}