package ru.kheynov.wordlemobile.domain.repository

import ru.kheynov.wordlemobile.domain.entities.Word
import ru.kheynov.wordlemobile.domain.util.Resource

interface WordleRepository {
    suspend fun getWord(language: String): Resource<Word>
}