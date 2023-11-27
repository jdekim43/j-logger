package kr.jadekim.logger.pipeline

import com.google.gson.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kr.jadekim.logger.Log
import kr.jadekim.logger.SerializedLog
import kr.jadekim.logger.ThrowableObjectLog
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.Type
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

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
        .registerTypeAdapter(ThrowableObjectLog::class.java, ThrowableObjectLogSerializer())
        .apply {
            if (!useCustomDateSerializer) {
                registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
            }
        }
        .create()

    override fun handle(log: Log): Log = SerializedLog.String(log, gson.toJson(log))

    private inner class LocalDateTimeSerializer : JsonSerializer<LocalDateTime> {

        override fun serialize(src: LocalDateTime?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            if (src == null) {
                return JsonNull.INSTANCE
            }

            val timestamp = src.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

            return JsonPrimitive(timestamp)
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

    private inner class ThrowableObjectLogSerializer : JsonSerializer<ThrowableObjectLog> {

        override fun serialize(
            src: ThrowableObjectLog,
            typeOfSrc: Type,
            context: JsonSerializationContext,
        ): JsonElement {
            if (src.throwable == null) {
                return JsonNull.INSTANCE
            }

            val fields = src.throwable!!::class.memberProperties
                .filter { it.visibility == KVisibility.PUBLIC && !it.isSuspend && it.name == "cause" }
                .associate { it.name to it.call(src.throwable) }

            return context.serialize(fields)
        }
    }
}
