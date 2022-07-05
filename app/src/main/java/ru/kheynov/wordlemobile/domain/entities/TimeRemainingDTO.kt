package ru.kheynov.wordlemobile.domain.entities

import kotlinx.serialization.Serializable


@Serializable
data class TimeRemainingDTO(
    val timeToNext: Int,
) {
    fun formatTime(): String {
        TODO("Formatting to normal")
    }
}
