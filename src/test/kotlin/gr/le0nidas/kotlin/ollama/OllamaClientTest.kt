package gr.le0nidas.kotlin.ollama

import gr.le0nidas.kotlin.ollama.engine.Engine
import gr.le0nidas.kotlin.ollama.request.GenerateRequest
import gr.le0nidas.kotlin.ollama.request.parameter.JsonFormat
import gr.le0nidas.kotlin.ollama.request.parameter.JsonSchemaFormat
import gr.leonidas.gr.le0nidas.kotlin.ollama.request.parameters.Model
import gr.leonidas.gr.le0nidas.kotlin.ollama.request.parameters.Prompt
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OllamaClientTest {

    @Test
    fun `by default the endpoint is the localhost on port 11434`() {
        val fakeEngine = FakeEngine()
        val client = OllamaClient(engine = fakeEngine)
        val dummyRequest = GenerateRequest.Builder(Model("test")).build(Prompt("test"))

        client.generate(dummyRequest)

        assertTrue(fakeEngine.endpoint.startsWith("http://localhost:11434"))
    }

    @Test
    fun `when provided with another url and port it uses those`() {
        val expectedEndpoint = "http://127.0.0.1:11000"
        val fakeEngine = FakeEngine()
        val client = OllamaClient(url = "http://127.0.0.1", port = 11000, engine = fakeEngine)
        val dummyRequest = GenerateRequest.Builder(Model("test")).build(Prompt("test"))

        client.generate(dummyRequest)

        assertTrue(fakeEngine.endpoint.startsWith(expectedEndpoint))
    }

    @Test
    fun `for generating a completion it calls the api-generate endpoint`() {
        val fakeEngine = FakeEngine()
        val client = OllamaClient(engine = fakeEngine)
        val dummyRequest = GenerateRequest.Builder(Model("test")).build(Prompt("test"))

        client.generate(dummyRequest)

        assertTrue(fakeEngine.endpoint.endsWith("/api/generate"))
    }

    @Test
    fun `the request is being posted to the engine as its body`() {
        val expectedBody = "{\"model\":\"code-test\",\"prompt\":\"write a test\",\"stream\":false,\"format\":null}"
        val fakeEngine = FakeEngine()
        val client = OllamaClient(engine = fakeEngine)
        val dummyRequest = GenerateRequest.Builder(Model("code-test")).build(Prompt("write a test"))

        client.generate(dummyRequest)

        assertEquals(expectedBody, fakeEngine.body)
    }

    @Test
    fun `the body has the proper format when the request has the json format`() {
        val expectedBody = "{\"model\":\"code-test\",\"prompt\":\"write a test\",\"stream\":false,\"format\":\"json\"}"
        val fakeEngine = FakeEngine()
        val client = OllamaClient(engine = fakeEngine)
        val dummyRequest =
            GenerateRequest.Builder(Model("code-test")).withFormat(JsonFormat).build(Prompt("write a test"))

        client.generate(dummyRequest)

        assertEquals(expectedBody, fakeEngine.body)
    }

    @Test
    fun `the body has the proper format when the request has the json schema format`() {
        val expectedBody = "{\"model\":\"code-test\",\"prompt\":\"write a test\",\"stream\":false,\"format\":{\"type\":\"object\",\"properties\":{\"name\":\"string\"}}}"
        val fakeEngine = FakeEngine()
        val client = OllamaClient(engine = fakeEngine)
        val dummyRequest = GenerateRequest.Builder(Model("code-test"))
            .withFormat(
                JsonSchemaFormat(
                    buildJsonObject {
                        put("type", "object")
                        put("properties", buildJsonObject {
                            put("name", "string")
                        })
                    }
                )
            )
            .build(Prompt("write a test"))

        client.generate(dummyRequest)

        assertEquals(expectedBody, fakeEngine.body)
    }

    @Test
    fun `returns the expected response`() {
        val expectedResponse = "Done!"
        val fakeEngine = FakeEngine(expectedResult = Result.success("{ \"response\": \"$expectedResponse\" }"))
        val client = OllamaClient(engine = fakeEngine)
        val dummyRequest = GenerateRequest.Builder(Model("code-test")).build(Prompt("write a test"))

        val actual = client.generate(dummyRequest)

        assertEquals(expectedResponse, actual.getOrThrow().value)
    }

    @Test
    fun `returns the provided error`() {
        val expectedError = "Oops!"
        val fakeEngine = FakeEngine(expectedResult = Result.failure(RuntimeException(expectedError)))
        val client = OllamaClient(engine = fakeEngine)
        val dummyRequest = GenerateRequest.Builder(Model("code-test")).build(Prompt("write a test"))

        val actual = client.generate(dummyRequest)

        assertEquals(expectedError, actual.exceptionOrNull()?.message)
    }

    private class FakeEngine(
        private val expectedResult: Result<String>? = null
    ) : Engine {
        var endpoint: String = ""
        var body: String = ""

        override fun post(endpoint: String, body: String): Result<String> {
            this.endpoint = endpoint
            this.body = body
            return expectedResult ?: Result.success("{ \"response\": \"Ok\" }")
        }
    }
}