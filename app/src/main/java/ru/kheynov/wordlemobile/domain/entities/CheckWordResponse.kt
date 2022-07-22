package ru.kheynov.wordlemobile.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckWordResponse(
//    @SerialName("lang") val lang: String,
//    val word: String,
    @SerialName("correct") val isCorrect: Boolean,
)
