package ru.kheynov.wordlemobile.data.repository

import retrofit2.Response
import ru.kheynov.wordlemobile.data.api.WordleApi
import ru.kheynov.wordlemobile.domain.entities.Word
import ru.kheynov.wordlemobile.domain.repository.WordleRepository
import javax.inject.Inject

class WordleRepositoryImpl @Inject constructor(
    private val wordleApi: WordleApi,
) : WordleRepository {

    var language = "ru"
        private set

    override suspend fun getWord(language: String): Response<Word> = wordleApi.getWord(language)
}