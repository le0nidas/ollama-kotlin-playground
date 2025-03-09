package gr.leonidas.gr.le0nidas.kotlin.ollama.request.parameters

@JvmInline
value class Prompt(val value: String) {
    init {
        require(value.isNotBlank()) { "Prompt cannot be blank" }
    }
}