package ru.kheynov.wordlemobile.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun Int.scaledSp(): TextUnit {
    val value: Int = this
    return with(LocalDensity.current) {
        val fontScale = this.fontScale
        val textSize = value / fontScale
        textSize.sp
    }
}

fun main() {
    val word = "кулак"
    val cells = listOf(
        Cell(0, 0, LetterState.MISS, 'к'),
        Cell(0, 0, LetterState.MISS, 'у'),
        Cell(0, 0, LetterState.MISS, 'к'),
        Cell(0, 0, LetterState.MISS, 'к'),
        Cell(0, 0, LetterState.MISS, 'а'),
    )
    println(cells.checkWord(word).toString())
}

fun List<Cell>.checkWord(word: String): List<LetterState> {
    val res = Array(5) { LetterState.MISS }
    val src = this.takeLast(5)
    val letters = word.toList().groupingBy { it }.eachCount().toMutableMap()
    src.apply {
        forEachIndexed { index, cell -> //searching for correct
            if (cell.letter == word[index]) {
                res[index] = LetterState.CORRECT
                letters[cell.letter] = letters[cell.letter]?.minus(1) ?: 0
            }
        }
        forEachIndexed { index, cell -> //searching for contained
            if (word.contains(cell.letter) && letters[cell.letter]!! > 0 && res[index] ==
                LetterState.MISS
            ) {
                res[index] = LetterState.CONTAINED
                letters[cell.letter] = letters[cell.letter]?.minus(1) ?: 0
            }
        }
    }
    return res.toList()
}