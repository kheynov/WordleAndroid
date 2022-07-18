package ru.kheynov.wordlemobile.presentation.util

import kotlinx.serialization.Serializable

@Serializable
data class GameResult(
    val language: String,
    val word: String,
    val cells: List<LetterState>,
    val timeToNext: Int,
) {
    fun getTimeToNext(currentTime: Long): String {
        val timeElapsed = timeToNext - currentTime
        val hours = timeElapsed / 3600
        val minutes = (timeElapsed % 3600) / 60
        val seconds = timeElapsed % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}