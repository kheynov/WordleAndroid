package ru.kheynov.wordlemobile.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    background = DarkBackground
)

private val LightColorPalette = lightColors(
    background = LightBackground,
    onSecondary = Color.Black,
    onBackground = Color.Black,


    /* Other default colors to override
    surface = Color.White,
    onPrimary = Color.White,
    onSurface = Color.Black,
    */
)

@Composable
fun WordleMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable
        () ->
    Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}