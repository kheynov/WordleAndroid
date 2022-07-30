package ru.kheynov.wordlemobile.presentation.screens.results_screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.delay
import ru.kheynov.wordlemobile.R
import ru.kheynov.wordlemobile.presentation.screens.game_screen.checkWordGuessed
import ru.kheynov.wordlemobile.presentation.theme.WordleMobileTheme
import ru.kheynov.wordlemobile.presentation.util.GameResult
import ru.kheynov.wordlemobile.presentation.util.LetterState

@Composable
fun ResultScreen(
    result: GameResult,
) {
    val timeRemaining = remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        while (true) {
            timeRemaining.value = result.getTimeToNext(System.currentTimeMillis() / 1000)
            delay(1000)
        }
    }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        val enterText = "Wordle(${result.language.uppercase()}) ${
            if (checkWordGuessed(result.cells.takeLast(5)))
                "${result.cells.size / 5}/6"
            else
                "❌"
        }"
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = enterText,
            fontSize = 30.sp
        )
        Text(modifier = Modifier.padding(vertical = 8.dp),
            text = result.word.uppercase(),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = getEmojis(result.cells)
        )
        val context = LocalContext.current
        Row(Modifier.fillMaxWidth(0.6f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            val resultsText = "$enterText\n\n${getEmojis(result.cells)
            }\n\n#вордли\nhttps://wordle.kheynov.ru/"
            Button(modifier = Modifier.weight(2f),
                onClick = {
                    copyToClipboard(context,
                        resultsText)
                    Toast.makeText(context, "Скопировано в буфер обмена", Toast.LENGTH_SHORT).show()
                }) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                    contentDescription = "")
            }
            Spacer(modifier = Modifier.width(5.dp))
            Button(
                modifier = Modifier.weight(5f),
                onClick = {
                    shareResults(context, resultsText)
                }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(modifier = Modifier.padding(end = 8.dp),
                        imageVector = Icons.Default.Share,
                        contentDescription
                        = "")
                    Text(text = "Поделиться")
                }
            }
        }

        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = "Следующее слово через:\n${timeRemaining.value}",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )
    }
}

fun copyToClipboard(context: Context, results: String) {
    val clipboardManager: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("", results)
    clipboardManager.setPrimaryClip(clip)
}

fun shareResults(context: Context, results: String) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, results)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(intent, null)
    startActivity(context, shareIntent, null)
}

fun getEmojis(result: List<LetterState>): String {
    var text = ""
    for (i in 1 until result.size + 1) {
        text += result[i - 1].emoji
        if (i % 5 == 0 && i != 0 && i != result.size) text += "\n"
    }
    return text
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ResultScreenPreview() {
    WordleMobileTheme {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp), color = MaterialTheme.colors
            .background) {
            ResultScreen(
                result = GameResult(
                    cells = listOf(
                        LetterState.CONTAINED,
                        LetterState.CONTAINED,
                        LetterState.CONTAINED,
                        LetterState.CONTAINED,
                        LetterState.CONTAINED,
                        //
                        LetterState.MISS,
                        LetterState.CONTAINED,
                        LetterState.CONTAINED,
                        LetterState.CONTAINED,
                        LetterState.MISS,
                        //
                        LetterState.MISS,
                        LetterState.CONTAINED,
                        LetterState.CONTAINED,
                        LetterState.CONTAINED,
                        LetterState.MISS,
                        //
                        LetterState.MISS,
                        LetterState.CONTAINED,
                        LetterState.CONTAINED,
                        LetterState.CONTAINED,
                        LetterState.MISS,
                        //
                    ),
                    timeToNext = 2,
                    language = "RU",
                    word = "удило"
                )
            )
        }
    }
}