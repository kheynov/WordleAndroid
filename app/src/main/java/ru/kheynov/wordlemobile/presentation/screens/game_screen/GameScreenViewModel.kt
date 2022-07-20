package ru.kheynov.wordlemobile.presentation.screens.game_screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.kheynov.wordlemobile.data.repository.WordleRepositoryImpl
import ru.kheynov.wordlemobile.presentation.util.Cell
import ru.kheynov.wordlemobile.presentation.util.GameResult
import ru.kheynov.wordlemobile.presentation.util.KeyboardLayout
import ru.kheynov.wordlemobile.presentation.util.Language
import ru.kheynov.wordlemobile.presentation.util.LetterState
import ru.kheynov.wordlemobile.presentation.util.SavedState
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
        fetchSavedState()
        keyboardLayout.value = KeyboardLayout.Russian
        language.value = Language.Russian.text
        viewModelScope.launch {
            loadWord()
        }

    }

    private fun fetchSavedState() {
        try {
            val res = Json.decodeFromString<List<SavedState>>(repository.state)
            Log.i(TAG, "saved state: $res")
            if (res.isNotEmpty()) {
                res.forEach {
                    if (it.language == language.value) {
                        _answerState = it.cellState.toMutableList()
                        _keyboardState = it.keyboardState.toMutableMap()
                        answerState.value = _answerState.toList()
                        keyboardState.value = _keyboardState.toMap()
                        columnCounter = _answerState.size / 5
                        rowCounter = _answerState.size % 5
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
        }
    }

    private suspend fun loadWord() {
        screenState.value = GameScreenState.Loading
        try {
            val response = repository.getWord(if (language.value.isNullOrEmpty()) Language.Russian
                .text else language.value!!)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    try {
                        delay(1000)
                        if (repository.results.isNotEmpty()) {
                            val lastResults =
                                Json.decodeFromString<List<GameResult>>(repository.results)
                            Log.i(TAG, "saved results: $lastResults")
                            lastResults.forEach {
                                if (it.word == response.body()?.word) {
                                    screenState.value = GameScreenState.Results(it)
                                    return@withContext
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, e.stackTraceToString())
                    }
                    response.body()?.word?.let {
                        if (it.isEmpty() || it.split(",").size == 1) return@let
                        if (
                            repository.lastWord != it.split(",")[0] &&
                            language.value.toString() == it.split(",")[1]
                        ) {
                            repository.saveWord(it, language.value.toString())
                            clearState()
                        }
                    }

                    screenState.value = GameScreenState.Loaded(response.body())
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
            viewModelScope.launch {
                loadWord()
            }
            clearState()
            fetchSavedState()
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
            answerState.value = _answerState.toList()
            Log.i(TAG, "state: ${answerState.value.toString()}")
        } else {
            Log.i(TAG, "End reached")
        }
    }

    fun eraseLetter() {
        if (rowCounter > 0) {
            _answerState.removeLast()
            rowCounter--
            answerState.value = _answerState.toList()
        } else {
            Log.i(TAG, "eraseLetter: start reached")
        }
    }

    fun enterWord() {
        if (rowCounter < 5) return
        columnCounter++
        rowCounter = 0
        Log.i(TAG, "State: ${screenState.value}")

        //TODO: Check word via API

        updateKeyboardState()

        Log.i(TAG, "checkWord: answerState: ${_answerState.toList().hashCode()}")
        answerState.value = emptyList()
        answerState.value = (_answerState.toList())
        keyboardState.value = mapOf()
        keyboardState.value = _keyboardState.toMap()

        saveState(SavedState(
            language = language.value.toString(),
            cellState = _answerState,
            keyboardState = _keyboardState
        ))
    }

    fun validateWord() {
        val results = _answerState.fold(mutableListOf<LetterState>()) { acc, v ->
            (acc + v.state) as MutableList<LetterState>
        }.toList()

        if (checkWordGuessed(results.takeLast(5))) {
            val gameResult = GameResult(
                cells = results,
                language = this.language.value.toString(),
                timeToNext = (screenState.value as GameScreenState.Loaded).data?.next ?: 0,
                word = (screenState.value as GameScreenState.Loaded).data?.word ?: "")
            screenState.postValue(GameScreenState.Results(gameResult))
            Log.i(TAG, Json.encodeToString(gameResult))
            saveResults(gameResult)
            return
        }

        if (columnCounter == 6) {
            Log.i(TAG, "checkWord: End reached")
            val gameResult = GameResult(
                cells = results,
                language = this.language.value.toString(),
                timeToNext = (screenState.value as GameScreenState.Loaded).data?.next ?: 0,
                word = (screenState.value as GameScreenState.Loaded).data?.word ?: "",
            )
            saveResults(gameResult)

            screenState.postValue(GameScreenState.Results(gameResult))

            clearState()
//            return
        }
    }

    private fun updateKeyboardState() {
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
    }

    private fun clearState() {
        _keyboardState.clear()
        keyboardState.value = _keyboardState.toMap()
        _answerState.clear()
        answerState.value = _answerState.toList()
        columnCounter = 0
        rowCounter = 0
    }

    private fun saveResults(results: GameResult) {
        Log.i(TAG, "saveResults/repository: ${repository.results}")
        var currentResults: MutableList<GameResult>? = emptyList<GameResult>().toMutableList()
        if (repository.results.isNotEmpty()) {
            currentResults =
                Json.decodeFromString<List<GameResult>>(repository.results).toMutableList()
            Log.i(TAG, "saveResults: currentResults: $currentResults")
            for (i in 0 until currentResults.size - 1) {
                if (currentResults[i].language == results.language) {
                    currentResults.removeAt(i)
                }
            }
        }
        currentResults?.add(results)
        repository.saveResults(Json.encodeToString(currentResults))
    }

    private fun saveState(state: SavedState) {
        Log.i(TAG, "repository.state: ${repository.state}")
        var currentState: MutableList<SavedState>? = emptyList<SavedState>().toMutableList()
        if (repository.state.isNotEmpty()) {
            currentState =
                Json.decodeFromString<List<SavedState>>(repository.state).toMutableList()
            for (i in 0 until currentState.size - 1) {
                if (currentState[i].language == state.language) {
                    currentState.removeAt(i)
                }
            }
        }
        currentState?.add(state)
        repository.saveState(Json.encodeToString(currentState))
    }

}

fun checkWordGuessed(word: List<LetterState>): Boolean {
    for (i in word) {
        if (i != LetterState.CORRECT) {
            return false
        }
    }
    return true
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

