package com.example.odpapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val entity: String,
    val canton: String?,
    val municipality: String?,
    val year: Int,
    val month: Int,
    val dateUpdate: String,
    val mjestoReg: String,
    val domVoz: Int,
    val strVoz: Int,
    val total: Int
)