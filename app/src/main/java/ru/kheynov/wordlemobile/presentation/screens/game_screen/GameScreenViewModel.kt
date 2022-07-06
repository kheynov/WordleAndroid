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
import javax.inject.Inject


private const val TAG = "GameScreenVM"

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val repository: WordleRepositoryImpl,
) : ViewModel() {

    var state = MutableLiveData<GameScreenState>()
        private set


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            Log.i(TAG, "Error: ${throwable.message}")
            state.postValue(GameScreenState.Error(throwable.message.toString()))
        }
    }

    fun loadWord() {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            state.postValue(GameScreenState.Loading)
            val response = repository.getWord(repository.language)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    state.postValue(GameScreenState.Loaded(response.body()))
                    Log.i(TAG, "loadWord: DATA, ${state.value.toString()}")
                    return@withContext
                }
                state.postValue(GameScreenState.Error(response.message()))
                Log.i(TAG, "loadWord: Error, ${state.value}")

            }
        }
    }
}