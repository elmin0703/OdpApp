package com.example.odpapp.model

import kotlinx.serialization.Serializable

@Serializable
data class ResidenceApiResponse(
    val errors: List<String>,
    val result: List<ResidenceData>,
    val success: Boolean
)
