package com.kiarah.snapstudy

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

// ---- Authorization Interceptor ----
class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(req)
    }
}

// ---- OpenAI Request/Response Models ----
data class OpenAIMessage(
    val role: String,
    val content: String
)

data class OpenAIRequest(
    val model: String = "gpt-4o-mini",
    val messages: List<OpenAIMessage>
)

data class OpenAIChoice(
    val message: OpenAIMessage
)

data class OpenAIResponse(
    val choices: List<OpenAIChoice>
)

// ---- Retrofit API Interface ----
interface OpenAIApi {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun createChatCompletion(
        @Body request: OpenAIRequest
    ): OpenAIResponse
}

// ---- Service Implementation ----
class ApiService(private val apiKey: String) {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(apiKey))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(OpenAIApi::class.java)

    suspend fun getHomeworkHelp(text: String, mode: UserMode): String {
        val systemPrompt = when (mode) {
            UserMode.STUDENT -> "Explain this homework step by step."
            UserMode.PARENT -> "Explain how a parent can help with this homework."
        }
        val request = OpenAIRequest(
            messages = listOf(
                OpenAIMessage("system", systemPrompt),
                OpenAIMessage("user", text)
            )
        )
        val response = api.createChatCompletion(request)
        return response.choices.firstOrNull()?.message?.content ?: "No response"
    }

    companion object {
        fun create(): ApiService = ApiService(BuildConfig.OPENAI_API_KEY)
    }
}
