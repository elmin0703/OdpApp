package com.example.odpapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vehicles")
data class FavoriteVehicleEntity(
    @PrimaryKey
    val id: Int,
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

fun FavoriteVehicleEntity.toVehicleEntity(): VehicleEntity {
    return VehicleEntity(
        id = this.id,
        entity = this.entity,
        canton = this.canton,
        municipality = this.municipality,
        year = this.year,
        month = this.month,
        dateUpdate = this.dateUpdate,
        mjestoReg = this.mjestoReg,
        domVoz = this.domVoz,
        strVoz = this.strVoz,
        total = this.total
    )
}