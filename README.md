This is an extremely simple library that helps me play around with ollama.

- It only supports generating a completion.
- The only available parameter is the `format` one.
- The response is not streamed. 

### Prerequisites

1. Download and install [ollama](https://ollama.com/download)
2. Run the server `ollama serve`

## Install
Follow [this guide](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package) to setup your project using published packages.

The package for this library is: `https://maven.pkg.github.com/le0nidas/ollama-kotlin-playground`

Add the dependency to your `build.gradle`:
```gradle
implementation("gr.le0nidas:ollama-kotlin-playground:0.0.1")
```

## Usage
```kotlin
fun main() {
    val ollamaClient = OllamaClient()
    val request = GenerateRequest.Builder(Model("llama3.2"))
        .build(prompt = Prompt("Generate three numbers from 0 to 100"))
    val response = ollamaClient.generate(request)

    response
        .onSuccess { println(it.value) }
        .onFailure { println(it.message) }
}

/*
Here are three random numbers between 0 and 100:

1. 43
2. 91
3. 18
 */
```

or if you need to set a json schema for formatting the response:
```kotlin
fun main() {
    val ollamaClient = OllamaClient()
    val request = GenerateRequest.Builder(Model("llama3.2"))
        .withFormat(JsonSchemaFormat(
            buildJsonObject {
                put("type", "object")
                put("properties", buildJsonObject {
                    put("numbers", buildJsonObject {
                        put("type", "array")
                    })
                })
            }
        ))
        .build(prompt = Prompt("Generate three numbers from 0 to 100"))
    val response = ollamaClient.generate(request)

    response
        .onSuccess { println(it.value) }
        .onFailure { println(it.message) }
}

/*
{
    "numbers": [
        43,
        91,
        13
    ]
}
 */
```

## FAQ
**Q:** Will it ever support anything else from the [API](https://github.com/ollama/ollama/blob/main/docs/api.md#api)?<br/>
**A:** Maybe. Depends on the needs of the _experiments_ I do that involve ollama.

**Q:** Why did you write it then?<br/>
**A:** I didn't want to copy code from project to project.
