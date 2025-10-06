package com.kiarah.snapstudy

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

// Request/response models for Gemini API (unchanged)
data class GeminiRequest(
    val contents: List<Content>
)
data class Content(
    val parts: List<Part>
)
data class Part(
    val text: String
)
data class GeminiResponse(
    val candidates: List<Candidate>
)
data class Candidate(
    val content: GeminiContent?
)
data class GeminiContent(
    val parts: List<GeminiPart>
)
data class GeminiPart(
    val text: String
)

// Gemini Retrofit API interface (UPDATED endpoint)
interface GeminiApi {
    @POST("models/gemini-1.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") key: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

// ApiService class (UPDATED baseUrl)
class ApiService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(GeminiApi::class.java)

    suspend fun getHomeworkHelp(text: String, mode: UserMode): String {
        val prompt = when (mode) {
            UserMode.STUDENT -> "Explain this homework step by step: $text"
            UserMode.PARENT -> "Explain how a parent can help with: $text"
        }

        val response = api.generateContent(
            "AIzaSyAfnABhqNveHCn70D2P14SWCOW9_IsiEVg", // Replace with your key in quotes
            GeminiRequest(
                contents = listOf(
                    Content(parts = listOf(Part(text = prompt)))
                )
            )
        )

        return response.candidates.firstOrNull()
            ?.content?.parts?.firstOrNull()?.text
            ?: "No response"
    }

    companion object {
        fun create(): ApiService = ApiService()
    }
}
