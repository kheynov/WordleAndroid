package ru.kheynov.wordlemobile.presentation.util

import kotlinx.serialization.Serializable

@Serializable
data class Cell(
    val x: Int,
    val y: Int,
    var state: LetterState,
    val letter: Char,
)
