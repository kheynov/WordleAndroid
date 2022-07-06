package ru.kheynov.wordlemobile.domain.repository

import retrofit2.Response
import ru.kheynov.wordlemobile.domain.entities.Word

interface WordleRepository {
    suspend fun getWord(language: String): Response<Word>
}