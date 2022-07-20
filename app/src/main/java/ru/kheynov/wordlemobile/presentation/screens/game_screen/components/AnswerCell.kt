package ru.kheynov.wordlemobile.presentation.screens.game_screen.components

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kheynov.wordlemobile.presentation.theme.CellColors
import ru.kheynov.wordlemobile.presentation.theme.DarkGridDividerColor
import ru.kheynov.wordlemobile.presentation.theme.LightGridDividerColor
import ru.kheynov.wordlemobile.presentation.theme.WordleMobileTheme
import ru.kheynov.wordlemobile.presentation.util.LetterState

private const val TAG = "AnswerCell"

@Composable
fun AnswerCell(
    modifier: Modifier = Modifier,
    state: LetterState,
    letter: Char?,
    animationDelay: Int = 0,
    onAnimationFinished: () -> Unit = {},
) {
    val isScaleAnimated = letter != ' '

    val isRotateAnimated = state != LetterState.NOT_USED

    val rotation = remember { Animatable(0f) }

    val scale = remember { Animatable(1f) }

    LaunchedEffect(isScaleAnimated) {
        scale.animateTo(if (isScaleAnimated) 1.08f else 1f, animationSpec = tween(150))
    }

    val targetRotate = 180f

    LaunchedEffect(isRotateAnimated) {
        rotation.animateTo(if (isRotateAnimated) targetRotate else 0f,
            animationSpec = tween(1200, animationDelay))
        if (targetRotate - rotation.value <= 10) {
            onAnimationFinished()
        }
    }

    Box(
        modifier = modifier
            .scale(scale.value)
            .graphicsLayer(rotationX = rotation.value)
            .background(
                if (targetRotate - rotation.value < 90)
                    CellColors(isSystemInDarkTheme()).getColor(state)!!
                else MaterialTheme.colors.background
            )
            .border(BorderStroke(
                1.dp,
                if (isSystemInDarkTheme()) DarkGridDividerColor else
                    LightGridDividerColor)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(modifier = Modifier
            .padding(4.dp)
            .graphicsLayer(rotationX = rotation.value),
            text =
            letter
                .toString()
                .uppercase(),
            fontSize
            = 40
                .sp)
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Composable
fun AnswerCellPreview() {
    WordleMobileTheme {
        Surface(modifier = Modifier
            .width(100.dp)
            .height(100.dp)
        ) {
            AnswerCell(state = LetterState.CONTAINED, letter = 'f')
        }
    }
}