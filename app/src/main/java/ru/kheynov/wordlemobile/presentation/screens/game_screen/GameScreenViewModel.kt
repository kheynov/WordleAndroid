package ru.kheynov.wordlemobile.presentation.screens.game_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.kheynov.wordlemobile.data.repository.WordleRepositoryImpl
import ru.kheynov.wordlemobile.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val repository: WordleRepositoryImpl,
    private val language: String,
) : ViewModel() {

    var state = MutableLiveData<GameScreenState>()
        private set

    fun loadWord() {
        viewModelScope.launch {
            when (val result = repository.getWord(language)) {
                is Resource.Success -> {
                    state.postValue(GameScreenState.Loaded(result.data))
                }
                is Resource.Error -> {
                    state.postValue(GameScreenState.Error(result.message.toString()))
                }
            }
        }
    }
}