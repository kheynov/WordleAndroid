package ru.kheynov.wordlemobile.data.repository

import ru.kheynov.wordlemobile.data.api.WordleApi
import ru.kheynov.wordlemobile.domain.entities.Word
import ru.kheynov.wordlemobile.domain.repository.WordleRepository
import ru.kheynov.wordlemobile.domain.util.Resource
import javax.inject.Inject

class WordleRepositoryImpl @Inject constructor(
    private val wordleApi: WordleApi,
) : WordleRepository {
    override suspend fun getWord(language: String): Resource<Word> {
        return try {
            val response = wordleApi.getWordAsync(language).await()
            if (response.isSuccessful) {
                Resource.Success(response.body())
            }
            Resource.Error(response.errorBody().toString())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message)
        }
    }
}