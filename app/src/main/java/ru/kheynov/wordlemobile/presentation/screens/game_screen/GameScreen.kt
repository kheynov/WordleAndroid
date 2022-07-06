package ru.kheynov.wordlemobile.presentation.screens.game_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kheynov.wordlemobile.presentation.screens.game_screen.components.Keyboard

@Composable
fun GameScreen(
    viewModel: GameScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.observeAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween) {
        Text(text = state.value.toString(), fontSize = 24.sp)
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable { viewModel.loadWord() },
            contentAlignment = Alignment.BottomCenter) {
            Keyboard()
        }
    }
}
