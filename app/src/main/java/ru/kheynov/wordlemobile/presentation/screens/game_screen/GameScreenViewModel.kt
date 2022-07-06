package ru.kheynov.wordlemobile.presentation.screens.game_screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kheynov.wordlemobile.data.repository.WordleRepositoryImpl
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

    private val _answerState: MutableList<Cell> = emptyList<Cell>().toMutableList()

    var answerState = MutableLiveData<List<Cell>>()
        private set

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            Log.i(TAG, "Error: ${throwable.message}")
            screenState.postValue(GameScreenState.Error(throwable.message.toString()))
        }
    }

    fun loadWord() {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
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
        }
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
        columnCounter++
        rowCounter = 0

        //TODO: checking word correctness
        if (columnCounter == 5) return //TODO: end reached
    }
}