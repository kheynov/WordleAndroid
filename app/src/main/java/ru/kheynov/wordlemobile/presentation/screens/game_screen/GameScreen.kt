package ru.kheynov.wordlemobile.presentation.screens.game_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.AnswerGrid
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.Header
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.Keyboard

@Composable
fun GameScreen(
    viewModel: GameScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.screenState.observeAsState()

    val answerGrid = viewModel.answerState.observeAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween) {
        Header(language = viewModel.language, onLanguageChange = {})
        AnswerGrid(state = answerGrid.value)
        Box(modifier = Modifier
            .wrapContentHeight()
            .clickable { viewModel.loadWord() },
            contentAlignment = Alignment.BottomCenter) {
            Keyboard(
                onErase = { viewModel.eraseLetter() },
                onEnter = { viewModel.checkWord() },
                onLetterClick = { letter -> viewModel.appendLetter(letter) }
            )
        }
    }
}
