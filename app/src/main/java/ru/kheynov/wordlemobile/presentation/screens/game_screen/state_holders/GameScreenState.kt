package ru.kheynov.wordlemobile.presentation.screens.game_screen.state_holders

import kotlinx.serialization.Serializable
import ru.kheynov.wordlemobile.domain.entities.Word
import ru.kheynov.wordlemobile.presentation.util.GameResult

sealed interface GameScreenState {
    object Loading : GameScreenState
    data class Loaded(val data: Word?) : GameScreenState
    data class Error(val error: String) : GameScreenState

    @Serializable
    data class Results(val results: GameResult) : GameScreenState
}