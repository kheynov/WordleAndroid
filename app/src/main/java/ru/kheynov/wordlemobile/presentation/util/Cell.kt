package ru.kheynov.wordlemobile.presentation.util

data class Cell(
    val x: Int,
    val y: Int,
    val state: LetterState,
    val letter: Char,
)
