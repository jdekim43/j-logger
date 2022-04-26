package kr.jadekim.logger.pipeline

import com.google.gson.*
import kr.jadekim.logger.Log
import java.io.OutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.Type
import java.util.*

class GsonFormatter(
    gson: Gson,
    var traceMaxLength: Int = 12,
    useCustomDateSerializer: Boolean = false,
) : JLogPipe {

    constructor(
        traceMaxLength: Int = 12,
        useCustomDateSerializer: Boolean = false,
    ) : this(Gson(), traceMaxLength, useCustomDateSerializer)

    companion object Key : JLogPipe.Key<GsonFormatter>

    override val key: JLogPipe.Key<out JLogPipe> = Key

    private val gson = gson.newBuilder()
        .registerTypeHierarchyAdapter(Throwable::class.java, ThrowableSerializer())
        .apply {
            if (!useCustomDateSerializer) {
                registerTypeAdapter(Date::class.java, DateSerializer())
            }
        }
        .create()

    override fun handle(log: Log): Log = TextFormatter.FormattedLog(log, gson.toJson(log))

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