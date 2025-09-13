package com.example.odpapp.model

import kotlinx.serialization.Serializable

@Serializable
data class VehicleRequest(
    val updateDate: String,
    val entityId: Int,
    val cantonId: Int,
    val municipalityId: Int,
    val year: String,
    val month: String
)
