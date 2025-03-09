package gr.le0nidas.kotlin.ollama.response

class GenerateResponse(val value: String) {
    init {
        require(value.isNotBlank()) { "Ollama response cannot be blank." }
    }
}