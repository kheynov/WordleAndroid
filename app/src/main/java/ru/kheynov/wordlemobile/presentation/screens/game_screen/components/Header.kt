package ru.kheynov.wordlemobile.presentation.screens.game_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ru.kheynov.wordlemobile.R
import ru.kheynov.wordlemobile.presentation.util.Language
import ru.kheynov.wordlemobile.presentation.util.scaledSp

@Composable
fun Header(
    modifier: Modifier = Modifier,
    language: String,
    onLanguageChange: (Language) -> Unit,
    isLoading: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        if (isLoading)
            Box(modifier = Modifier.weight(2f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(45.dp),
                    color = MaterialTheme.colors.onBackground,
                )
            }
        else
            Spacer(modifier = Modifier.weight(2f))

        Text(modifier = Modifier.weight(5f),
            text = "Wordle(${language.uppercase()})",
            fontSize = 36.scaledSp(),
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

                DropdownMenuItem(onClick = {
                    expanded = false
                    onLanguageChange(Language.Russian)
                }) {
                    Text(Language.Russian.text.uppercase())
                }

                DropdownMenuItem(onClick = {
                    expanded = false
                    onLanguageChange(Language.English)
                }) {
                    Text(Language.English.text.uppercase())
                }
            }
        }
    }
}
