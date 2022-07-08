package ru.kheynov.wordlemobile.presentation.util

data class Cell(
    val x: Int,
    val y: Int,
    var state: LetterState,
    val letter: Char,
)
