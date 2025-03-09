package gr.le0nidas.kotlin.ollama.request

import gr.le0nidas.kotlin.ollama.request.parameter.Format
import gr.le0nidas.kotlin.ollama.request.parameter.TextFormat
import gr.le0nidas.kotlin.ollama.request.parameter.Model
import gr.le0nidas.kotlin.ollama.request.parameter.Prompt

class GenerateRequest private constructor(
    val model: Model,
    val prompt: Prompt,
    val format: Format
) {

    class Builder(
        private val model: Model
    ) {
        private var format: Format = TextFormat

        fun withFormat(format: Format): Builder = this.apply { this.format = format }

        fun build(prompt: Prompt): GenerateRequest {
            return GenerateRequest(
                model = model,
                prompt = prompt,
                format = format
            )
        }
    }
}