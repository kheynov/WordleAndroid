package ru.kheynov.wordlemobile.presentation.util

import ru.kheynov.wordlemobile.presentation.util.Key.*

object KeyboardLayout {
    val Russian = listOf(
        listOf(
            Letter('й'),
            Letter('ц'),
            Letter('у'),
            Letter('к'),
            Letter('е'),
            Letter('н'),
            Letter('г'),
            Letter('ш'),
            Letter('щ'),
            Letter('з'),
            Letter('х'),
            Letter('ъ'),
        ),
        listOf(
            Letter('ф'),
            Letter('ы'),
            Letter('в'),
            Letter('а'),
            Letter('п'),
            Letter('р'),
            Letter('о'),
            Letter('л'),
            Letter('д'),
            Letter('ж'),
            Letter('э'),
        ),
        listOf(
            Erase,
            Letter('я'),
            Letter('ч'),
            Letter('с'),
            Letter('м'),
            Letter('и'),
            Letter('т'),
            Letter('ь'),
            Letter('б'),
            Letter('ю'),
            Enter
        )
    )
}

sealed interface Key {
    data class Letter(val char: Char) : Key
    object Erase : Key
    object Enter : Key
}