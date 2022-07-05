package ru.kheynov.wordlemobile.data.api

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kheynov.wordlemobile.domain.entities.Word

interface WordleApi {
    @GET("word")
    suspend fun getWordAsync(
        @Query("lang") language: String = "ru",
    ): Deferred<Response<Word>>
}