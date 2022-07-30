package ru.kheynov.wordlemobile.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kheynov.wordlemobile.domain.entities.CheckWordResponse
import ru.kheynov.wordlemobile.domain.entities.Word

interface WordleApi {
    @GET("get")
    suspend fun getWord(
        @Query("lang") language: String = "ru",
    ): Response<Word>

    @GET("check")
    suspend fun checkWord(
        @Query("word") word: String,
        @Query("lang") language: String,
    ): Response<CheckWordResponse>
}