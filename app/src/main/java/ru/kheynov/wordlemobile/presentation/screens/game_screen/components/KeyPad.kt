package ru.kheynov.wordlemobile.presentation.screens.game_screen.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kheynov.wordlemobile.R
import ru.kheynov.wordlemobile.presentation.theme.DarkKeyPadTextColor
import ru.kheynov.wordlemobile.presentation.theme.KeyboardColors
import ru.kheynov.wordlemobile.presentation.theme.LightKeyPadTextColor
import ru.kheynov.wordlemobile.presentation.theme.WordleMobileTheme
import ru.kheynov.wordlemobile.presentation.util.Key
import ru.kheynov.wordlemobile.presentation.util.LetterState

@Composable
fun KeyPad(
    modifier: Modifier = Modifier,
    state: LetterState? = null,
    key: Key,
    onClick: (Key) -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(KeyboardColors[state ?: LetterState.NOT_USED]!!)
            .padding(horizontal = 4.dp, vertical = 16.dp)
            .clickable {
                when (key) {
                    is Key.Erase -> onClick(Key.Erase)
                    is Key.Enter -> onClick(Key.Enter)
                    is Key.Letter -> onClick(key)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        when (key) {
            is Key.Erase -> Image(painter = painterResource(id = R.drawable.ic_backspace),
                contentDescription = "Keypad " +
                        "picture",
                colorFilter =
                if (!isSystemInDarkTheme())
                    ColorFilter.tint(LightKeyPadTextColor)
                else
                    null
            )
            is Key.Enter -> Image(painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "Keypad " +
                        "picture",
                colorFilter =
                if (!isSystemInDarkTheme())
                    ColorFilter.tint(LightKeyPadTextColor)
                else
                    null
            )
            else -> Text(
                text = if (key is Key.Enter) "Ввод" else (key as Key.Letter).char.toString(),
                color = if (isSystemInDarkTheme())
                    DarkKeyPadTextColor
                else
                    LightKeyPadTextColor,
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun KeyPadPreview() {
    WordleMobileTheme {
        Surface(modifier = Modifier
            .width(100.dp)
            .height(100.dp)) {
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                KeyPad(
                    modifier = Modifier,
                    key = Key.Enter,
                    state = LetterState.CORRECT,
                )
            }
        }
    }
}