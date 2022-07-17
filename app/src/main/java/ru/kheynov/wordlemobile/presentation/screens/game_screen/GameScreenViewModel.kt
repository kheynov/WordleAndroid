package ru.kheynov.wordlemobile.presentation.screens.game_screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kheynov.wordlemobile.data.repository.WordleRepositoryImpl
import ru.kheynov.wordlemobile.presentation.util.Cell
import ru.kheynov.wordlemobile.presentation.util.KeyboardLayout
import ru.kheynov.wordlemobile.presentation.util.Language
import ru.kheynov.wordlemobile.presentation.util.LetterState
import javax.inject.Inject

private const val TAG = "GameScreenVM"

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val repository: WordleRepositoryImpl,
) : ViewModel() {

    val keyboardLayout = MutableLiveData(KeyboardLayout.Russian)


    private var rowCounter = 0


    private var columnCounter = 0

    val language = MutableLiveData(repository.language ?: Language.Russian.text)

    var screenState = MutableLiveData<GameScreenState>()
        private set

    private var _keyboardState: MutableMap<Char, LetterState> = mutableMapOf()

    var keyboardState = MutableLiveData<Map<Char, LetterState>?>()
        private set

    private var _answerState: MutableList<Cell> = emptyList<Cell>().toMutableList()

    var answerState = MutableLiveData<List<Cell>>()
        private set

    init {
        viewModelScope.launch {
            loadWord()
        }
        keyboardLayout.value = KeyboardLayout.Russian
        language.value = Language.Russian.text
    }

    private suspend fun loadWord() {
        screenState.postValue(GameScreenState.Loading)
        try {
            val response = repository.getWord(if (language.value.isNullOrEmpty()) Language.Russian
                .text else language.value!!)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    screenState.postValue(GameScreenState.Loaded(response.body()))
                    answerState.postValue(mutableListOf())
                    Log.i(TAG, "loadWord: DATA, ${screenState.value.toString()}")
                    return@withContext
                }
                screenState.value = GameScreenState.Error(response.message())
            }
        } catch (e: Exception) {
            screenState.value = GameScreenState.Error(e.localizedMessage?.toString()
                ?: "Unknown error")
        }
        Log.i(TAG, "state: ${screenState.value}")
    }

    fun changeLanguage(language: Language) {
        if (this.language.value != language.text) {
            repository.language = language.text
            this.language.value = language.text
            keyboardLayout.value = when (language) {
                Language.English -> KeyboardLayout.English
                Language.Russian -> KeyboardLayout.Russian
            }
            clearState()
            viewModelScope.launch {
                loadWord()
            }
        }
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
        if (rowCounter < 5) return
        columnCounter++
        rowCounter = 0
        Log.i(TAG, "State: ${screenState.value}")

        val result = _answerState.checkWord(word = (screenState.value as GameScreenState.Loaded)
            .data?.word.toString())

        Log.i(TAG, "checkWord: answerState: ${_answerState.toList().hashCode()}")

        for (i in _answerState.indices) {
            if (i < _answerState.size - 5) continue

            val cell = _answerState[i]
            val state = result[i % 5]
            cell.state = state
            if (_keyboardState.containsKey(cell.letter)) {
                if (
                    _keyboardState[cell.letter] == LetterState.CORRECT ||
                    _keyboardState[cell.letter] == LetterState.MISS
                ) continue
            }
            _keyboardState[cell.letter] = cell.state
        }

        Log.i(TAG, "checkWord: answerState: ${_answerState.toList().hashCode()}")
        answerState.value = emptyList()
        answerState.value = (_answerState.toList())
        keyboardState.value = mapOf()
        keyboardState.value = _keyboardState.toMap()

        //TODO: update keyboard state

        //TODO: check if all letters are correct

        if (columnCounter == 5) {
            Log.i(TAG, "checkWord: End reached")
            clearState()
            return
        } //TODO: end reached
    }

    private fun clearState() {
        _keyboardState.clear()
        keyboardState.value = _keyboardState.toMap()
        _answerState.clear()
        answerState.value = _answerState.toList()
        columnCounter = 0
        rowCounter = 0
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
