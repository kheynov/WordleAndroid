package ru.kheynov.wordlemobile.presentation.screens.game_screen

sealed interface WordCheckState {
    object Checking : WordCheckState
    object Correct : WordCheckState
    object Incorrect : WordCheckState
    object Idle : WordCheckState
}