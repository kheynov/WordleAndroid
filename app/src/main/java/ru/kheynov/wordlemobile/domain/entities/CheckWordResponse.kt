package ru.kheynov.wordlemobile.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckWordResponse(
    @SerialName("lang") val language: String,
    val word: String,
    @SerialName("is_correct") val isCorrect: Boolean,
)
