package ru.kheynov.wordlemobile.presentation.util

import kotlinx.serialization.Serializable

@Serializable
data class SavedState(
    val language: String,
    val cellState: List<Cell>,
    val keyboardState: Map<Char, LetterState>,
)
