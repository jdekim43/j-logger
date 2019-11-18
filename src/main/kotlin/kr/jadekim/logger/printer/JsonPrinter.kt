package kr.jadekim.logger.printer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.jadekim.logger.model.Log
import java.io.OutputStream
import java.io.PrintWriter
import java.io.StringWriter

class JsonPrinter(
    mapper: ObjectMapper = jacksonObjectMapper(),
    private val output: OutputStream = System.out,
    override var printStackTrace: Boolean = true,
    var traceMaxLength: Int = 12
) : LogPrinter {

    companion object {
        private const val NEWLINE_CHARACTER = '\n'.toInt()
    }

    private val throwableModule = SimpleModule().apply {
        addSerializer(Throwable::class.java, ThrowableSerializer())
    }

    private val mapper = mapper.copy()
        .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
        .registerModule(throwableModule)

    override fun print(log: Log) {
        mapper.writeValue(output, log)
        output.write(NEWLINE_CHARACTER)
        output.flush()
    }

    private inner class ThrowableSerializer : JsonSerializer<Throwable>() {

        override fun serialize(value: Throwable, gen: JsonGenerator, serializers: SerializerProvider) {
            if (traceMaxLength == Int.MAX_VALUE) {
                val writer = StringWriter()
                value.printStackTrace(PrintWriter(writer))
                gen.writeString(writer.toString())
                return
            }

            val builder = StringBuilder()

            builder.appendln(value.toString())

            value.stackTrace
                .take(traceMaxLength)
                .forEach {
                    builder.appendln("\tat $it")
                }

            gen.writeString(builder.toString())
        }
    }
}