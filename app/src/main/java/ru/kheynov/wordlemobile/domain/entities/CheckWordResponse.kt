package ru.kheynov.wordlemobile.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckWordResponse(
    @SerialName("correct") val isCorrect: Boolean,
)
