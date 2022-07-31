package ru.kheynov.wordlemobile.presentation.screens.game_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kheynov.wordlemobile.R


@Composable
fun LoadingBlock(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator(
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colors.onBackground
        )

        Text(text = stringResource(R.string.loading_text), fontSize = 24.sp)
    }
}