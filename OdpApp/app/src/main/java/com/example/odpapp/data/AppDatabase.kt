package com.example.odpapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [
    ResidenceEntity::class,
    VehicleEntity::class,
    FavoriteResidenceEntity::class,
    FavoriteVehicleEntity::class
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun residenceDao(): ResidenceDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun favoriteResidenceDao(): FavoriteResidenceDao
    abstract fun favoriteVehicleDao(): FavoriteVehicleDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "odp.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}