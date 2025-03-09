package gr.le0nidas.kotlin.ollama.request.parameter

@JvmInline
value class Prompt(val value: String) {
    init {
        require(value.isNotBlank()) { "Prompt cannot be blank" }
    }
}