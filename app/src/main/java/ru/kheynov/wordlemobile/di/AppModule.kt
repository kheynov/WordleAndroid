@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package ru.kheynov.wordlemobile.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.kheynov.wordlemobile.data.api.WordleApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl(): String = "https://wordle.kheynov.ru/api/word/"

    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL: String): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideWordleApi(retrofit: Retrofit): WordleApi = retrofit.create(WordleApi::class.java)

}