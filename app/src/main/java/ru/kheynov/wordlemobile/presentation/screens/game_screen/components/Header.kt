package ru.kheynov.wordlemobile.presentation.screens.game_screen.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kheynov.wordlemobile.R
import ru.kheynov.wordlemobile.presentation.theme.WordleMobileTheme
import ru.kheynov.wordlemobile.presentation.util.Language

@Composable
fun Header(
    modifier: Modifier = Modifier,
    language: String,
    onLanguageChange: (Language) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Spacer(modifier = Modifier.weight(2f))
        Text(modifier = Modifier.weight(5f), text = "Wordle($language)", fontSize = 36.sp,
            textAlign = TextAlign.Center)
        Box(modifier = Modifier.weight(2f)) {

            IconButton(onClick = { expanded = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_language_24),
                    contentDescription = "Показать меню",
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.Center)
                )
            }
            DropdownMenu(

                modifier = Modifier,
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(x = 20.dp, y = 10.dp)
            ) {
                Language.values().forEach { language ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onLanguageChange(language)
                    }) {
                        Text(language.text.uppercase())
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Composable
fun HeaderPreview() {
    WordleMobileTheme {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)) {
            Header(language = Language.RUSSIAN.text, onLanguageChange = {})
        }
    }
}