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
import java.util.Date

@Deprecated("Use JacksonPrinter")
typealias JsonPrinter = JacksonPrinter

class JacksonPrinter(
    mapper: ObjectMapper = jacksonObjectMapper(),
    output: OutputStream = System.out,
    override var printStackTrace: Boolean = true,
    var traceMaxLength: Int = 12,
    useCustomDateSerializer: Boolean = false
) : LogPrinter {

    private val writer = PrintWriter(output)

    private val timestampModule = SimpleModule().apply {
        addSerializer(Date::class.java, DateSerializer())
    }

    private val throwableModule = SimpleModule().apply {
        addSerializer(Throwable::class.java, ThrowableSerializer())
    }

    private val mapper = mapper.copy()
        .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
        .registerModule(throwableModule)

    init {
        if (!useCustomDateSerializer) {
            mapper.registerModule(timestampModule)
        }
    }

    override fun print(log: Log) {
        mapper.writeValue(writer, log)
        writer.println()
        writer.flush()
    }

    private inner class DateSerializer : JsonSerializer<Date>() {

        override fun serialize(value: Date?, gen: JsonGenerator, serializers: SerializerProvider) {
            value?.let { gen.writeNumber(it.time) }
        }
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