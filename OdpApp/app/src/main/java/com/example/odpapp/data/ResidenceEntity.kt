package com.example.odpapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "residence")
data class ResidenceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val entitet: String,
    val canton: String?,
    val municipality: String?,
    val institution: String,
    val dateUpdate: String,
    val saTrajPreb: Int,
    val saPrivPreb: Int,
    val total: Int
)