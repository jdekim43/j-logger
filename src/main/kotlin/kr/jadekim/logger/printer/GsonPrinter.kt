package kr.jadekim.logger.printer

import com.google.gson.*
import kr.jadekim.logger.model.Log
import java.io.OutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.Type
import java.util.Date

class GsonPrinter(
    gson: Gson = Gson(),
    output: OutputStream = System.out,
    override var printStackTrace: Boolean = true,
    var traceMaxLength: Int = 12,
    useCustomDateSerializer: Boolean = false
) : LogPrinter {

    private val writer = PrintWriter(output)

    private val gson = gson.newBuilder()
        .registerTypeAdapter(Throwable::class.java, ThrowableSerializer())
        .apply {
            if (!useCustomDateSerializer) {
                registerTypeAdapter(Date::class.java, DateSerializer())
            }
        }
        .create()

    override fun print(log: Log) {
        gson.toJson(log, writer)
        writer.println()
        writer.flush()
    }

    private inner class DateSerializer : JsonSerializer<Date> {

        override fun serialize(src: Date?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return src?.let { JsonPrimitive(it.time) } ?: JsonNull.INSTANCE
        }
    }

    private inner class ThrowableSerializer : JsonSerializer<Throwable> {

        override fun serialize(src: Throwable, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            if (traceMaxLength == Int.MAX_VALUE) {
                val writer = StringWriter()
                src.printStackTrace(PrintWriter(writer))
                return JsonPrimitive(writer.toString())
            }

            val builder = StringBuilder()

            builder.appendLine(src.toString())

            src.stackTrace
                .take(traceMaxLength)
                .forEach {
                    builder.appendLine("\tat $it")
                }

            return JsonPrimitive(builder.toString())
        }
    }
}