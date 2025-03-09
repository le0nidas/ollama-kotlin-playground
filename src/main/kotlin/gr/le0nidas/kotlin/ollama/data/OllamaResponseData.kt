package gr.le0nidas.kotlin.ollama.data

import kotlinx.serialization.Serializable

@Serializable
internal class OllamaResponseData(
    val response: String
)