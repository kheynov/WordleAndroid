package ru.kheynov.wordlemobile.presentation.screens.game_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kheynov.wordlemobile.R
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.AnswerGrid
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.ErrorBlock
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.Header
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.Keyboard
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.LoadingBlock
import ru.kheynov.wordlemobile.presentation.screens.game_screen.state_holders.GameScreenState
import ru.kheynov.wordlemobile.presentation.screens.game_screen.state_holders.WordCheckState
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

    val keyboardState = viewModel.keyboardState.observeAsState()

    val checkState = viewModel.checkingState.observeAsState()

    Log.i(TAG, "GameScreen: RECOMPOSITION")

    val context = LocalContext.current

    LaunchedEffect(checkState.value) {
        if (checkState.value == WordCheckState.Incorrect) {
            Toast.makeText(context, context.getString(R.string.word_not_exist), Toast
                .LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Header(
            modifier = Modifier.wrapContentHeight(),
            language = language.value ?: Language.Russian.text,
            onLanguageChange = {
                viewModel.changeLanguage(it)
            },
            isLoading = checkState.value == WordCheckState.Checking
        )
        when (state.value) {
            is GameScreenState.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        ErrorBlock(state = state.value as GameScreenState.Error)
                    }
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter) {
                        Button(
                            onClick = { viewModel.updateWord() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp),
                        ) {
                            Text(text = stringResource(R.string.retry_button).uppercase())
                        }
                    }
                }
            }
            is GameScreenState.Results ->
                ResultScreen(result = (state.value as GameScreenState.Results).results)
            is GameScreenState.Loading ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingBlock(modifier = Modifier)
                }
            else -> {
                Box(modifier = Modifier
                    .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(modifier = Modifier
                        .fillMaxSize()
                    ) {
                        item {
                            AnswerGrid(
                                state = answerGrid.value,
                                onAnimationFinished = { viewModel.showResultsIfNeeded() })
                        }
                    }
                }

                //Keyboard block
                Box(modifier = Modifier.requiredHeight(200.dp),
                    contentAlignment = Alignment.BottomCenter) {
                    Keyboard(
                        modifier = Modifier,
                        isActive = state.value is GameScreenState.Loaded,
                        layout = keyboardLayout.value!!,
                        state = keyboardState.value,
                        onErase = { viewModel.eraseLetter() },
                        onEnter = { viewModel.enterWord() },
                        onLetterClick = { letter -> viewModel.appendLetter(letter) }
                    )
                }
            }
        }


    }
}
