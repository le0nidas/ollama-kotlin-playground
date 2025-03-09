package gr.le0nidas.kotlin.ollama

import gr.le0nidas.kotlin.ollama.data.OllamaFormat.OllamaJson
import gr.le0nidas.kotlin.ollama.data.OllamaFormat.OllamaJsonSchema
import gr.le0nidas.kotlin.ollama.data.OllamaRequestData
import gr.le0nidas.kotlin.ollama.data.OllamaResponseData
import gr.le0nidas.kotlin.ollama.engine.Engine
import gr.le0nidas.kotlin.ollama.engine.OkHttpEngine
import gr.le0nidas.kotlin.ollama.request.GenerateRequest
import gr.le0nidas.kotlin.ollama.request.parameter.JsonFormat
import gr.le0nidas.kotlin.ollama.request.parameter.JsonSchemaFormat
import gr.le0nidas.kotlin.ollama.request.parameter.TextFormat
import gr.le0nidas.kotlin.ollama.response.GenerateResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OllamaClient(
    url: String = "http://localhost",
    port: Int = 11434,
    engine: Engine? = null
) {
    private val postUrl = "$url:$port/api/generate"
    private val innerEngine: Engine = engine ?: OkHttpEngine()
    private val json = Json { ignoreUnknownKeys = true }

    fun generate(request: GenerateRequest): Result<GenerateResponse> {
        val requestData = OllamaRequestData(
            model = request.model.value,
            prompt = request.prompt.value,
            stream = false,
            format = when (request.format) {
                TextFormat -> null
                JsonFormat -> OllamaJson
                is JsonSchemaFormat -> OllamaJsonSchema(request.format.schema)
            }
        )

        val response = innerEngine.post(
            endpoint = postUrl,
            body = Json.encodeToString(requestData)
        )

        if (response.isFailure) {
            return Result.failure(response.exceptionOrNull()!!)
        }
        return response
            .map { json.decodeFromString<OllamaResponseData>(it) }
            .map { GenerateResponse(it.response) }
    }
}
