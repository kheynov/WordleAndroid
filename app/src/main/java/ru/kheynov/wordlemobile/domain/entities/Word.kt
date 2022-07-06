package ru.kheynov.wordlemobile.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Word(
    @SerialName("lang") val language: String,
    val word: String,
    val timeToNext: Int,
)
