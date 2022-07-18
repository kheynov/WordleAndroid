package ru.kheynov.wordlemobile.presentation.util

enum class LetterState(val emoji: String) {
    CORRECT("🟩"),
    CONTAINED("🟨"),
    MISS("⬛️"),
    NOT_USED("⬜️"),
}