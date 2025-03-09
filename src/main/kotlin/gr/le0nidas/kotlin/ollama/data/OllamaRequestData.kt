package gr.le0nidas.kotlin.ollama.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject

@Serializable
internal class OllamaRequestData(
    val model: String,
    val prompt: String,
    val stream: Boolean,
    val format: OllamaFormat?
)

@Serializable(with = OllamaFormatKSerializer::class)
internal sealed interface OllamaFormat {
    data object OllamaJson : OllamaFormat
    class OllamaJsonSchema(val value: JsonObject) : OllamaFormat
}

private class OllamaFormatKSerializer : KSerializer<OllamaFormat> {
    override val descriptor: SerialDescriptor
        get() = JsonObject.serializer().descriptor

    override fun deserialize(decoder: Decoder): OllamaFormat {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: OllamaFormat) {
        when (value) {
            OllamaFormat.OllamaJson -> encoder.encodeString("json")
            is OllamaFormat.OllamaJsonSchema -> encoder.encodeSerializableValue(JsonObject.serializer(), value.value)
        }
    }
}
