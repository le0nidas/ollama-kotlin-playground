package gr.le0nidas.kotlin.ollama.engine

interface Engine {
    fun post(endpoint: String, body: String): Result<String>
}