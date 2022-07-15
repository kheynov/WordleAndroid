package ru.kheynov.wordlemobile.presentation.theme

import androidx.compose.ui.graphics.Color
import ru.kheynov.wordlemobile.presentation.util.LetterState
import ru.kheynov.wordlemobile.presentation.util.LetterState.*

val DarkKeyPadTextColor = Color(0xFFF5F5F5)
val LightKeyPadTextColor = Color(0xFFFFFFFF)

val DarkBackground = Color(0xFF121213)
val LightBackground = Color(0xFFFFFFFF)

val DarkGridDividerColor = Color(0xFF404040)
val LightGridDividerColor = Color(0xFFD4D4D4)

private val DarkMiss = Color(0xFF73737A)
private val DarkKeyboardMiss = Color(0xFF000000)


private val LightMiss = Color(0xFF2F3031)
private val LightKeyboardMiss = Color(0xFF2F3031)

private val DarkNotUsed = Color(0xFF818384)
private val LightNotUsed = Color(0xFF818384) //TODO

data class KeyboardColors(
    val isDarkTheme: Boolean = true,
) {
    private val colors = mapOf(
        CORRECT to Color(0xFF6AAA64),
        CONTAINED to Color(0xFFC9B458),
        MISS to if (isDarkTheme) DarkKeyboardMiss else LightKeyboardMiss,
        NOT_USED to if (isDarkTheme) DarkNotUsed else LightNotUsed,
    )

    fun getColor(state: LetterState) = colors[state]
}

data class CellColors(
    val isDarkTheme: Boolean = true,
) {
    private val colors = mapOf(
        CORRECT to Color(0xFF6AAA64),
        CONTAINED to Color(0xFFC9B458),
        MISS to if (isDarkTheme) DarkMiss else LightMiss,
        NOT_USED to if (isDarkTheme) DarkBackground else LightBackground,
    )
    fun getColor(state: LetterState) = colors[state]
}