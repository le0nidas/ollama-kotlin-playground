package gr.le0nidas.kotlin.ollama.engine

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

internal class OkHttpEngine : Engine {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .callTimeout(60, TimeUnit.SECONDS)
        .build()

    override fun post(endpoint: String, body: String): Result<String> {
        val request = Request.Builder()
            .url(endpoint)
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            return Result.failure(Exception("Request failed with code ${response.code} and message: ${response.message}"))
        }

        val responseBody = response.body?.string()
            ?: return Result.failure(Exception("Response body was null"))

        return Result.success(responseBody)
    }
}