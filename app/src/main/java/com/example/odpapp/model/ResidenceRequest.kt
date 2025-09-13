package com.example.odpapp.model

import kotlinx.serialization.Serializable

@Serializable
data class ResidenceRequest(
    val updateDate: String,
    val entityId: Int,
    val cantonId: Int,
    val municipalityId: Int
)
