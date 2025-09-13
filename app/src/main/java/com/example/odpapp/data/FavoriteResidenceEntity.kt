package com.example.odpapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_residence")
data class FavoriteResidenceEntity(
    @PrimaryKey
    val id: Int,
    val entitet: String,
    val canton: String?,
    val municipality: String?,
    val institution: String,
    val dateUpdate: String,
    val saTrajPreb: Int,
    val saPrivPreb: Int,
    val total: Int
)

fun FavoriteResidenceEntity.toResidenceEntity(): ResidenceEntity {
    return ResidenceEntity(
        id = this.id,
        entitet = this.entitet,
        canton = this.canton,
        municipality = this.municipality,
        institution = this.institution,
        dateUpdate = this.dateUpdate,
        saTrajPreb = this.saTrajPreb,
        saPrivPreb = this.saPrivPreb,
        total = this.total
    )
}