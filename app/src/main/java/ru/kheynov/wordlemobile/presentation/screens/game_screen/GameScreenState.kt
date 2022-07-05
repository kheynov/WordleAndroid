package ru.kheynov.wordlemobile.presentation.screens.game_screen

import ru.kheynov.wordlemobile.domain.entities.Word
import ru.kheynov.wordlemobile.domain.util.Resource

sealed interface GameScreenState {
    object Loading : GameScreenState
    data class Loaded(val data: Word?) : GameScreenState
    data class Error(val error: String) : GameScreenState
}