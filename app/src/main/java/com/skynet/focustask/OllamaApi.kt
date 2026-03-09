package com.skynet.focustask.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

data class OllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = false
)

data class OllamaResponse(
    val response: String
)

interface OllamaApiService {
    @POST("api/generate")
    suspend fun generateFeedback(@Body request: OllamaRequest): OllamaResponse
}

object OllamaClient {
    private const val BASE_URL = "http://10.0.2.2:11434/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val api: OllamaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OllamaApiService::class.java)
    }
}