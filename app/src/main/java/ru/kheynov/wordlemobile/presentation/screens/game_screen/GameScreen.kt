package ru.kheynov.wordlemobile.presentation.screens.game_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.AnswerGrid
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.Header
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.Keyboard
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.LoadingBlock
import ru.kheynov.wordlemobile.presentation.screens.results_screen.ResultScreen
import ru.kheynov.wordlemobile.presentation.util.Language

private const val TAG = "GameScreen"

@Composable
fun GameScreen(
    viewModel: GameScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.screenState.observeAsState()

    val language = viewModel.language.observeAsState()

    val keyboardLayout = viewModel.keyboardLayout.observeAsState()

    val answerGrid = viewModel.answerState.observeAsState()

    Log.i(TAG, "GameScreen: RECOMPOSITION")

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween) {
        Header(
            language = language.value ?: Language.Russian.text,
            onLanguageChange = {
                viewModel.changeLanguage(it)
            },
            isLoading = false/*state.value is GameScreenState.Loading*/ //TODO: loading if checking
            // word
        )
        when (state.value) {
            is GameScreenState.Error -> Text(text = "Ошибка: ${
                (state.value as GameScreenState
                .Error).error
            }",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 16.dp)
            )
            is GameScreenState.Results ->
                ResultScreen(result = (state.value as GameScreenState.Results).results)
            is GameScreenState.Loading ->
                LoadingBlock()
            else -> {
                AnswerGrid(state = answerGrid.value)
            }
        }
        //Keyboard block
        Box(modifier = Modifier
            .wrapContentHeight(),
            contentAlignment = Alignment.BottomCenter) {
            Keyboard(
                isActive = state.value is GameScreenState.Loaded,
                layout = keyboardLayout.value!!,
                state = viewModel.keyboardState.value,
                onErase = { viewModel.eraseLetter() },
                onEnter = { viewModel.enterWord() },
                onLetterClick = { letter -> viewModel.appendLetter(letter) }
            )
        }

    }
}
