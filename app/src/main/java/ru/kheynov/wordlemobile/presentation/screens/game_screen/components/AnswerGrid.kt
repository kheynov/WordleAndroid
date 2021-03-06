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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kheynov.wordlemobile.presentation.theme.WordleMobileTheme
import ru.kheynov.wordlemobile.presentation.util.Cell
import ru.kheynov.wordlemobile.presentation.util.LetterState

@Composable
fun AnswerGrid(
    modifier: Modifier = Modifier,
    state: List<Cell>?,
    size: Pair<Int, Int> = Pair(6, 5),
    onAnimationFinished: () -> Unit = {},
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.fillMaxWidth()) {
            repeat(size.first) { x ->
                Row(Modifier
                    .fillMaxWidth()) {
                    repeat(size.second) { y ->
                        val cellState = state?.find { cell -> cell.x == x && cell.y == y }
                        AnswerCell(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp),
                            state = cellState?.state ?: LetterState.NOT_USED,
                            letter = cellState?.letter ?: ' ',
                            animationDelay = y * 200,
                            onAnimationFinished = {
                                if (y >= 4) {
//                                    Log.i(TAG, "AnswerGrid: Animation FINISHED")
                                    onAnimationFinished()
                                } else Unit
                            }
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
        Cell(0, 0, LetterState.MISS, '??'),
        Cell(0, 1, LetterState.MISS, '??'),
        Cell(0, 2, LetterState.CONTAINED, '??'),
        Cell(0, 3, LetterState.MISS, '??'),
        Cell(0, 4, LetterState.MISS, '??'),
    )
    /*listOf(
       Pair(LetterState.MISS, '??'),
       Pair(LetterState.CORRECT, '??'),
       Pair(LetterState.MISS, '??'),
       Pair(LetterState.MISS, '??'),
       Pair(LetterState.MISS, '??'),
   ),
   listOf(
       Pair(LetterState.CONTAINED, '??'),
       Pair(LetterState.CONTAINED, '??'),
       Pair(LetterState.MISS, '??'),
       Pair(LetterState.CONTAINED, '??'),
       Pair(LetterState.MISS, '??'),
   ),
  listOf(
       Pair(LetterState.CONTAINED, '??'),
       Pair(LetterState.CONTAINED, '??'),
       Pair(LetterState.CORRECT, '??'),
       Pair(LetterState.MISS, '??'),
       Pair(LetterState.MISS, '??'),
   ),
   listOf(
       Pair(LetterState.CORRECT, '??'),
       Pair(LetterState.CORRECT, '??'),
       Pair(LetterState.CORRECT, '??'),
       Pair(LetterState.CORRECT, '??'),
       Pair(LetterState.CORRECT, '??'),
   ),*/

    WordleMobileTheme {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)) {
            AnswerGrid(state = testState)
        }
    }
}
