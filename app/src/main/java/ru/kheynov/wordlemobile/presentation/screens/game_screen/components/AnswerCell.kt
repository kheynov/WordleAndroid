package ru.kheynov.wordlemobile.presentation.screens.game_screen.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kheynov.wordlemobile.presentation.theme.CellColors
import ru.kheynov.wordlemobile.presentation.theme.DarkGridDividerColor
import ru.kheynov.wordlemobile.presentation.theme.LightGridDividerColor
import ru.kheynov.wordlemobile.presentation.theme.WordleMobileTheme
import ru.kheynov.wordlemobile.presentation.util.LetterState

@Composable
fun AnswerCell(
    modifier: Modifier = Modifier,
    state: LetterState,
    letter: Char?,
) {
    Box(
        modifier = modifier
            .defaultMinSize(40.dp, 40.dp)
            .background(CellColors(isSystemInDarkTheme()).getColor(state)!!)
            .border(BorderStroke(
                1.dp,
                if (isSystemInDarkTheme()) DarkGridDividerColor else
                    LightGridDividerColor)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(modifier = Modifier.padding(4.dp), text = letter.toString().uppercase(), fontSize = 40
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