package gr.leonidas.gr.le0nidas.kotlin.ollama.request.parameters

@JvmInline
value class Model(val value: String) {
    init {
        require(value.isNotBlank()) { "Model cannot be blank" }
    }
}