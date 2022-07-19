package ru.kheynov.wordlemobile.presentation.screens.game_screen.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kheynov.wordlemobile.presentation.theme.WordleMobileTheme
import ru.kheynov.wordlemobile.presentation.util.Cell
import ru.kheynov.wordlemobile.presentation.util.LetterState

@Composable
fun AnswerGrid(
    state: List<Cell>?,
    size: Pair<Int, Int> = Pair(6, 5),
    onAnimationFinished: () -> Unit = {},
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            repeat(size.first) { x ->
                Row(Modifier.fillMaxWidth()) {
                    repeat(size.second) { y ->
                        val cellState = state?.find { cell -> cell.x == x && cell.y == y }
                        AnswerCell(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp),
                            state = cellState?.state ?: LetterState.NOT_USED,
                            letter = cellState?.letter ?: ' ',
                            animationDelay = y * 200,
                            onAnimationFinished = onAnimationFinished
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Composable
fun AnswerGridPreview() {
    val testState = listOf(
        Cell(0, 0, LetterState.MISS, 'а'),
        Cell(0, 1, LetterState.MISS, 'б'),
        Cell(0, 2, LetterState.CONTAINED, 'о'),
        Cell(0, 3, LetterState.MISS, 'б'),
        Cell(0, 4, LetterState.MISS, 'а'),
    )
    /*listOf(
       Pair(LetterState.MISS, 'к'),
       Pair(LetterState.CORRECT, 'и'),
       Pair(LetterState.MISS, 'ш'),
       Pair(LetterState.MISS, 'к'),
       Pair(LetterState.MISS, 'а'),
   ),
   listOf(
       Pair(LetterState.CONTAINED, 'р'),
       Pair(LetterState.CONTAINED, 'о'),
       Pair(LetterState.MISS, 'ж'),
       Pair(LetterState.CONTAINED, 'о'),
       Pair(LetterState.MISS, 'к'),
   ),
  listOf(
       Pair(LetterState.CONTAINED, 'п'),
       Pair(LetterState.CONTAINED, 'о'),
       Pair(LetterState.CORRECT, 'р'),
       Pair(LetterState.MISS, 'к'),
       Pair(LetterState.MISS, 'а'),
   ),
   listOf(
       Pair(LetterState.CORRECT, 'с'),
       Pair(LetterState.CORRECT, 'и'),
       Pair(LetterState.CORRECT, 'р'),
       Pair(LetterState.CORRECT, 'о'),
       Pair(LetterState.CORRECT, 'п'),
   ),*/

    WordleMobileTheme {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)) {
            AnswerGrid(state = testState)
        }
    }
}
