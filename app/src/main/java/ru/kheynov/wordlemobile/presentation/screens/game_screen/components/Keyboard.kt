package ru.kheynov.wordlemobile.presentation.screens.game_screen.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    state: Map<Char, LetterState>? = null,
    layout: List<List<Key>> = KeyboardLayout.Russian,
    onErase: () -> Unit = {},
    onEnter: () -> Unit = {},
    onLetterClick: (Char) -> Unit = {},
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(4.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (key in layout[0]) {
                    KeyPad(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 3.dp),
                        key = key,
                        state = state?.get((key as Key.Letter).char) ?: LetterState.NOT_USED,
                        onClick = { letter ->
                            if (isActive)
                                onLetterClick((letter as Key.Letter).char)
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.Center) {
                for (key in layout[1]) {
                    KeyPad(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 3.dp),
                        key = key,
                        state = state?.get((key as Key.Letter).char) ?: LetterState.NOT_USED,
                        onClick = { letter ->
                            if (isActive)
                                onLetterClick((letter as Key.Letter).char)
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.Center) {
                for (key in layout[2]) {
                    when (key) {
                        is Key.Letter -> KeyPad(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 3.dp),
                            key = key,
                            state = state?.get(key.char) ?: LetterState.NOT_USED,
                            onClick = { letter ->
                                if (isActive)
                                    onLetterClick((letter as Key.Letter).char)
                            },
                        )
                        else -> KeyPad(modifier = Modifier
                            .weight(1.7f)
                            .padding(horizontal = 3.dp),
                            key = key,
                            onClick = {
                                if (isActive) {
                                    if (it is Key.Erase) onErase()
                                    else onEnter()
                                }
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
fun KeyboardPreview() {
    WordleMobileTheme {
        Surface(modifier = Modifier.background(MaterialTheme.colors.background)) {
            Keyboard(state = mapOf(
                'ы' to LetterState.CORRECT,
                'а' to LetterState.CONTAINED,
                'л' to LetterState.MISS
            ))
        }
    }
}