package gr.le0nidas.kotlin.ollama.request.parameter

@JvmInline
value class Model(val value: String) {
    init {
        require(value.isNotBlank()) { "Model cannot be blank" }
    }
}