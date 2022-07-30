package ru.kheynov.wordlemobile.data.repository

import retrofit2.Response
import ru.kheynov.wordlemobile.data.api.WordleApi
import ru.kheynov.wordlemobile.data.local.PrefStorage
import ru.kheynov.wordlemobile.domain.entities.CheckWordResponse
import ru.kheynov.wordlemobile.domain.entities.Word
import ru.kheynov.wordlemobile.domain.repository.WordleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordleRepositoryImpl @Inject constructor(
    private val wordleApi: WordleApi,
    private val prefStorage: PrefStorage,
) : WordleRepository {

    var language = prefStorage.language

    val results: String
        get() = prefStorage.results.toString()

    fun saveResults(result: String) {
        prefStorage.results = result
    }

    val state: String
        get() = prefStorage.state.toString()

    fun saveState(state: String) {
        prefStorage.state = state
    }

    fun clearState() {
        prefStorage.state = ""
    }

    val lastWord: String
        get() = prefStorage.lastWord.toString()

    fun saveWord(word: String, language: String) {
        prefStorage.lastWord = "$word,$language"
    }

    override suspend fun getWord(language: String): Response<Word> = wordleApi.getWord(language)

    override suspend fun checkWord(language: String, word: String): Response<CheckWordResponse> =
        wordleApi.checkWord(word = word, language = language)
}