package ru.kheynov.wordlemobile.presentation.screens.game_screen.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kheynov.wordlemobile.presentation.theme.WordleMobileTheme
import ru.kheynov.wordlemobile.presentation.util.Key
import ru.kheynov.wordlemobile.presentation.util.KeyboardLayout
import ru.kheynov.wordlemobile.presentation.util.LetterState

@Composable
fun Keyboard(
    state: Map<Char, LetterState>,
    layout: List<List<Key>> = KeyboardLayout.Russian,
    onErase: () -> Unit = {},
    onEnter: () -> Unit = {},
    onLetterClick: () -> Unit = {},
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)
        .padding(bottom = 4.dp)
    ) {
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceEvenly) {
            LazyRow(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceEvenly) {
                items(layout[0]) { key ->
                    KeyPad(key = key,
                        state = state[(key as Key.Letter).char] ?: LetterState.NOT_USED)
                }
            }
            LazyRow(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceEvenly) {
                items(layout[1]) { key ->
                    KeyPad(key = key,
                        state = state[(key as Key.Letter).char] ?: LetterState.NOT_USED)
                }
            }
            LazyRow(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceEvenly) {
                items(layout[2]) { key ->
                    when (key) {
                        is Key.Letter -> KeyPad(key = key,
                            state = state[key.char] ?: LetterState.NOT_USED)
                        else -> KeyPad(key = key)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun KeyboardPreview() {
    WordleMobileTheme {
        Keyboard(state = mapOf('ы' to LetterState.CORRECT))
    }
}