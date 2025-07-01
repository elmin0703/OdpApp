package com.example.odpapp.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ApiResponse(
    val errors: List<String>,
    val result: List<Dataset>,
    val success: Boolean
)