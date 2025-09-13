package com.example.odpapp.model

import kotlinx.serialization.Serializable

@Serializable
data class ResidenceData(
    val entity: String,
    val canton: String?,
    val municipality: String?,
    val institution: String?,
    val dateUpdate: String,
    val withPermanentResidenceTotal: Int,
    val withTemporaryResidenceTotal: Int,
    val total: Int
)
