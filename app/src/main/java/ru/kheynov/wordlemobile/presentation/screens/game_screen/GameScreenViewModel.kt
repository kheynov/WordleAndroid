package ru.kheynov.wordlemobile.presentation.screens.game_screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.kheynov.wordlemobile.data.repository.WordleRepositoryImpl
import ru.kheynov.wordlemobile.domain.entities.Word
import ru.kheynov.wordlemobile.presentation.util.Cell
import ru.kheynov.wordlemobile.presentation.util.Language
import ru.kheynov.wordlemobile.presentation.util.LetterState
import javax.inject.Inject

private const val TAG = "GameScreenVM"

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val repository: WordleRepositoryImpl,
) : ViewModel() {

    private var rowCounter = 0


    private var columnCounter = 0
    var language: String = repository.language
        private set

    var screenState = MutableLiveData<GameScreenState>()
        private set

    var keyboardState = MutableLiveData<Map<Char, LetterState>?>()


    init {
        loadWord()
    }

    private var _answerState: MutableList<Cell> = emptyList<Cell>().toMutableList()

    var answerState = MutableLiveData<List<Cell>>()
        private set

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            Log.i(TAG, "Error: ${throwable.message}")
            screenState.postValue(GameScreenState.Error(throwable.message.toString()))
        }
    }

    fun loadWord() {
        /*CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            screenState.postValue(GameScreenState.Loading)
            val response = repository.getWord(language)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    screenState.postValue(GameScreenState.Loaded(response.body()))
                    answerState.postValue(mutableListOf())
                    Log.i(TAG, "loadWord: DATA, ${screenState.value.toString()}")
                    return@withContext
                }
                screenState.postValue(GameScreenState.Error(response.message()))
                Log.i(TAG, "loadWord: Error, ${screenState.value}")

            }
        }*/

        //DEBUG
        screenState.value = GameScreenState.Loaded(Word("ru", "сироп", 234))
        //---
    }

    fun changeLanguage(language: Language) {
        this.language = language.text
        loadWord()
    }

    fun appendLetter(letter: Char) {
        if (rowCounter <= 4) {
            _answerState.add(
                Cell(
                    columnCounter,
                    rowCounter,
                    LetterState.NOT_USED,
                    letter
                )
            )
            rowCounter++
            answerState.postValue(_answerState.toList())
            Log.i(TAG, "state: ${answerState.value.toString()}")
        } else {
            Log.i(TAG, "End reached")
        }
    }

    fun eraseLetter() {
        if (rowCounter > 0) {
            _answerState.removeLast()
            rowCounter--
            answerState.postValue(_answerState.toList())
        } else {
            Log.i(TAG, "eraseLetter: start reached")
        }
    }

    fun checkWord() {
        if (rowCounter < 4) return
        columnCounter++
        rowCounter = 0
        Log.i(TAG, "State: ${screenState.value}")

        val result = _answerState.checkWord(word = (screenState.value as GameScreenState.Loaded)
            .data?.word.toString())

        Log.i(TAG, "checkWord: answerState: ${_answerState.toList().hashCode()}")

        for (i in _answerState.indices) {
            if (i < _answerState.size - 5) continue
            _answerState[i].state = result[i % 5]
        }

        Log.i(TAG, "checkWord: answerState: ${_answerState.toList().hashCode()}")
        answerState.value = emptyList()
        answerState.value = (_answerState.toList())

        //TODO: update keyboard state

        //TODO: check if all letters are correct

        if (columnCounter == 5) {
            Log.i(TAG, "checkWord: End reached")
            return
        } //TODO: end reached
    }

}

fun List<Cell>.checkWord(word: String): List<LetterState> {
    val res: MutableList<LetterState> = emptyList<LetterState>().toMutableList()
    this.takeLast(5).forEachIndexed { index, cell ->
        if (cell.letter == word[index]) {
            res.add(LetterState.CORRECT)
        } else if (word.contains(cell.letter)) {
            res.add(LetterState.CONTAINED)
        } else {
            res.add(LetterState.MISS)
        }
    }
    return res.toList()
}