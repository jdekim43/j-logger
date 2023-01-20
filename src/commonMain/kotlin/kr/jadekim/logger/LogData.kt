package kr.jadekim.logger

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kr.jadekim.logger.context.EmptyLogContext
import kr.jadekim.logger.context.LogContext

interface Log {
    val loggerName: String
    val level: LogLevel
    val message: String
    val throwable: Throwable?
    val meta: Map<String, Any?>
    val context: LogContext
    val timestamp: LocalDateTime

    fun isPrintable(level: LogLevel) = this.level.isPrintableAt(level)
}

data class LogData(
    override val loggerName: String,
    override val level: LogLevel,
    override val message: String,
    override val throwable: Throwable? = null,
    override val meta: Map<String, Any?> = emptyMap(),
    override val context: LogContext = EmptyLogContext,
    override val timestamp: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
) : Log

sealed class SerializedLog<T>(log: Log, val data: T) : Log by log {

    class String(log: Log, data: kotlin.String) : SerializedLog<kotlin.String>(log, data)

    @Suppress("unused")
    class ByteArray(log: Log, data: kotlin.ByteArray) : SerializedLog<kotlin.ByteArray>(log, data)
}
