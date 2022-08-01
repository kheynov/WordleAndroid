package ru.kheynov.wordlemobile.presentation.screens.game_screen.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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

    val isRotateAnimated = language == Language.English.text

    val rotation = remember { Animatable(0f) }

    val targetRotate = 180f



    LaunchedEffect(isRotateAnimated) {
        rotation.animateTo(if (isRotateAnimated) targetRotate else 0f,
            animationSpec = tween(400))
    }

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
            text = "Wordle", /*(${language.uppercase()})*/
            fontSize = 36.scaledSp(),
            textAlign = TextAlign.Center)
        Box(modifier = Modifier.weight(2f)) {

            IconButton(
                modifier = Modifier.graphicsLayer(
                    rotationY = rotation.value,

                ),
                onClick = {
                    if (language == Language.Russian.text)
                        onLanguageChange(Language.English)
                    else onLanguageChange(Language.Russian)
                }
            ) {
                Image(
                    painter = if (targetRotate - rotation.value < 90) {
                    painterResource(id = R.drawable.ic_en_flag)
                } else painterResource(id = R.drawable.ic_ru_flag),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
