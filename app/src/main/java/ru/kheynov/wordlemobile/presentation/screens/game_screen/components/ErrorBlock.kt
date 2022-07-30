package ru.kheynov.wordlemobile.presentation.screens.game_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kheynov.wordlemobile.R
import ru.kheynov.wordlemobile.presentation.screens.game_screen.state_holders.GameScreenState


@Composable
fun ErrorBlock(
    modifier: Modifier = Modifier,
    state: GameScreenState.Error,
) {
    val isConnectionError = state.error.contains("connect") ||
            state.error.contains("hostname") ||
            state.error.contains("timeout")

    val errorText = if (isConnectionError) "Неустойчивое сетевое соединение, проверьте ваше " +
            "подключение к интернету и попробуйте снова\n\nИнформация для разработчика:\n${state
                .error}"
    else "Произошла ошибка\n\nИнформация для разработчика: ${state.error}"

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = if (isConnectionError) R.drawable.ic_no_connection else
            R.drawable.ic_error), contentDescription = null, contentScale = ContentScale.Fit)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = errorText, fontSize = 18.sp, textAlign = TextAlign.Center, modifier =
        Modifier.padding(horizontal = 16.dp))
    }

}