package com.example.odpapp.model

import kotlinx.serialization.Serializable

@Serializable
data class VehicleApiResponse(
    val errors: List<String>,
    val result: List<VehicleData>,
    val success: Boolean
)
