package ru.kheynov.wordlemobile.data.repository

import retrofit2.Response
import ru.kheynov.wordlemobile.data.api.PrefStorage
import ru.kheynov.wordlemobile.data.api.WordleApi
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

    var results = prefStorage.results
        private set

    fun saveResults(result: String) {
        prefStorage.results = result
    }

    override suspend fun getWord(language: String): Response<Word> = wordleApi.getWord(language)
}