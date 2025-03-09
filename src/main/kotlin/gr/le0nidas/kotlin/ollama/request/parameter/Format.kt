package gr.le0nidas.kotlin.ollama.request.parameter

import kotlinx.serialization.json.JsonObject

sealed interface Format

data object TextFormat : Format
data object JsonFormat : Format
data class JsonSchemaFormat(val schema: JsonObject) : Format