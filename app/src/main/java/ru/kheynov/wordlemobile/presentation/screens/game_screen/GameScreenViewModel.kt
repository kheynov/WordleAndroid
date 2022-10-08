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
import ru.kheynov.wordlemobile.presentation.screens.game_screen.state_holders.GameScreenState
import ru.kheynov.wordlemobile.presentation.screens.game_screen.state_holders.WordCheckState
import ru.kheynov.wordlemobile.presentation.util.Cell
import ru.kheynov.wordlemobile.presentation.util.GameResult
import ru.kheynov.wordlemobile.presentation.util.KeyboardLayout
import ru.kheynov.wordlemobile.presentation.util.Language
import ru.kheynov.wordlemobile.presentation.util.LetterState
import ru.kheynov.wordlemobile.presentation.util.SavedState
import ru.kheynov.wordlemobile.presentation.util.checkWord
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

    var checkingState = MutableLiveData<WordCheckState>()
        private set

    private var _keyboardState: MutableMap<Char, LetterState> = mutableMapOf()

    var keyboardState = MutableLiveData<Map<Char, LetterState>?>()
        private set

    private var _answerState: MutableList<Cell> = emptyList<Cell>().toMutableList()

    var answerState = MutableLiveData<List<Cell>>()
        private set

    fun updateWord() {
        viewModelScope.launch {
            loadWord()
        }
    }

    init {
        keyboardLayout.value = KeyboardLayout.Russian
        language.value = Language.Russian.text
        fetchSavedState()
        updateStates()

        viewModelScope.launch {
            loadWord()
        }

        checkingState.observeForever {
            Log.i(TAG, "checkingState changed: $it")
            if (it == WordCheckState.Correct) {
                Log.i(TAG, "CORRECT: ")
                validateWord()
            }
        }
    }

    private fun fetchSavedState() {
        try {
            val res = Json.decodeFromString<List<SavedState>>(repository.state)
//            Log.i(TAG, "saved state: $res")
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
                    val word = response.body()

                    response.body()?.word?.let {
                        if (repository.lastWord.isEmpty() || repository.lastWord.split(",").size == 1) {
                            repository.saveWord(it, language.value.toString())
                            clearState()
                            repository.clearState()
                            return@let
                        }
                        if (
                            repository.lastWord.split(",")[0] != it &&
                            language.value.toString() == repository.lastWord.split(",")[1]
                        ) {
                            repository.saveWord(it, language.value.toString())
                            clearState()
                            repository.clearState()
                        }
                    }

                    screenState.value = GameScreenState.Loaded(word)
//                    Log.i(TAG, "loadWord: DATA, ${screenState.value.toString()}")
                    return@withContext
                }
                screenState.value = GameScreenState.Error(response.message())
            }
        } catch (e: Exception) {
            screenState.value = GameScreenState.Error(e.localizedMessage?.toString()
                ?: "Unknown error")
        }
    }

    private fun checkWord() {
        viewModelScope.launch {
            val word = _answerState.fold(mutableListOf<Char>()) { acc, v ->
                (acc + v.letter) as MutableList<Char>
            }.toList().takeLast(5).joinToString("")

            checkingState.value = WordCheckState.Checking
            try {
                val response = repository.checkWord(
                    language =
                    if (language.value.isNullOrEmpty())
                        Language.Russian.text
                    else language.value!!,
                    word = word
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (response.body()?.isCorrect == true) {
                            checkingState.value = WordCheckState.Correct
                            validateWord()
                        } else {
                            checkingState.value = WordCheckState.Incorrect
                        }
                    } else {
                        Log.i(TAG, "checkWord: ${response.message()}")
                        checkingState.value = WordCheckState.Incorrect
                    }
                    delay(1000)
                    checkingState.value = WordCheckState.Idle
                }
            } catch (e: Exception) {
                Log.e(TAG, "checkWord error", e)
            }
        }
    }

    fun showResultsIfNeeded() {
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
//            Log.i(TAG, Json.encodeToString(gameResult))
            saveResults(gameResult)
            return
        }

        if (columnCounter == 6) {
//            Log.i(TAG, "checkWord: End reached")
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
            updateStates()
        }
    }

    private fun updateStates() {
        answerState.value = emptyList()
        answerState.value = (_answerState.toList())
        keyboardState.value = mapOf()
        keyboardState.value = _keyboardState.toMap()
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
//            Log.i(TAG, "state: ${answerState.value.toString()}")
        } else {
//            Log.i(TAG, "End reached")
        }
    }

    fun eraseLetter() {
        if (rowCounter > 0) {
            _answerState.removeLast()
            rowCounter--
            answerState.value = _answerState.toList()
        } /*else {
            Log.i(TAG, "eraseLetter: start reached")
        }*/
    }

    fun enterWord() {
        if (rowCounter < 5) return
        checkWord()
    }

    private fun validateWord() {
        if (rowCounter < 5) return
        columnCounter++
        rowCounter = 0
//        Log.i(TAG, "State: ${screenState.value}")
        //TODO: Check word via API

        updateKeyboardState()

//        Log.i(TAG, "checkWord: answerState: ${_answerState.toList().hashCode()}")
        updateStates()

        saveState(SavedState(
            language = language.value.toString(),
            cellState = _answerState,
            keyboardState = _keyboardState
        ))
    }

    private fun updateKeyboardState() {
        val result = _answerState.checkWord(word = (screenState.value as GameScreenState.Loaded)
            .data?.word.toString())

        for (i in _answerState.indices) {
            if (i < _answerState.size - 5) continue

            val cell = _answerState[i]
            val state = result[i % 5]
            cell.state = state
            if (cell.state == LetterState.CORRECT) {
                _keyboardState[cell.letter] = cell.state
                continue
            }
            if (_keyboardState.containsKey(cell.letter)) {
                if (
                    _keyboardState[cell.letter] == LetterState.CORRECT ||
                    _keyboardState[cell.letter] == LetterState.MISS
                ) continue
            }
            if (
                cell.state != LetterState.CORRECT && _keyboardState[cell.letter] == LetterState
                    .CONTAINED
            ) continue
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
        var currentResults: MutableList<GameResult>? = emptyList<GameResult>().toMutableList()
        if (repository.results.isNotEmpty()) {
            currentResults =
                Json.decodeFromString<List<GameResult>>(repository.results).toMutableList()
            for (i in 0 until currentResults.size) {
                if (currentResults[i].language == results.language) {
                    currentResults.removeAt(i)
                }
            }
        }
        currentResults?.add(results)
        repository.saveResults(Json.encodeToString(currentResults))
    }

    private fun saveState(state: SavedState) {
        var currentState: MutableList<SavedState>? = emptyList<SavedState>().toMutableList()
        if (repository.state.isNotEmpty()) {
            currentState =
                Json.decodeFromString<List<SavedState>>(repository.state).toMutableList()
            for (i in 0 until currentState.size) {
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



