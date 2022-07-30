package ru.kheynov.wordlemobile.domain.repository

import retrofit2.Response
import ru.kheynov.wordlemobile.domain.entities.CheckWordResponse
import ru.kheynov.wordlemobile.domain.entities.Word

interface WordleRepository {
    suspend fun getWord(language: String): Response<Word>

    suspend fun checkWord(language: String, word: String): Response<CheckWordResponse>
}