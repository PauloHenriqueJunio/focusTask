package com.skynet.focustask.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

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
    // 10.0.2.2 é o IP mágico para o emulador Android acessar o localhost do seu notebook!
    private const val BASE_URL = "http://10.0.2.2:11434/"

    val api: OllamaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OllamaApiService::class.java)
    }
}