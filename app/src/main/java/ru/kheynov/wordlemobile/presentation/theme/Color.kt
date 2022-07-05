package ru.kheynov.wordlemobile.presentation.theme

import android.hardware.lights.Light
import androidx.compose.ui.graphics.Color
import ru.kheynov.wordlemobile.presentation.util.LetterState.*

val CellTextColor = Color(0xFFFFFFFF)

val DarkKeyPadTextColor = Color(0xFFF5F5F5)
val LightKeyPadTextColor = Color(0xFFFFFFFF)

val DarkBackground = Color(0xFF121213)
val LightBackground = Color(0xFFFFFFFF)

val DarkGridDividerColor = Color(0xFF404040)
val LightGridDividerColor = Color(0xFFD4D4D4)

private val DarkMiss = Color(0xFF3A3A3C)
private val LightMiss = Color(0xFF3A3A3C) //TODO

private val DarkNotUsed = Color(0xFF818384)
private val LightNotUsed = Color(0xFF818384) //TODO

val KeyboardColors = mapOf(
    CORRECT to Color(0xFF6AAA64),
    CONTAINED to Color(0xFFC9B458),
    MISS to DarkMiss,
    NOT_USED to DarkNotUsed,
)

