package kr.jadekim.logger.integration

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kr.jadekim.logger.JLog
import kr.jadekim.logger.LogData
import kr.jadekim.logger.LogLevel
import kr.jadekim.logger.context.LogContext
import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.LogRecord

class JulLogger : Handler() {

    private val logger = JLog.get("JulLogger")

    private val Level.jlogLevel: LogLevel?
        get() = when(this) {
            Level.OFF -> LogLevel.NONE
            Level.SEVERE -> LogLevel.ERROR
            Level.WARNING -> LogLevel.WARNING
            Level.INFO -> LogLevel.INFO
            Level.CONFIG, Level.FINE -> LogLevel.DEBUG
            Level.ALL, Level.FINER, Level.FINEST -> LogLevel.TRACE
            else -> null
        }

    override fun publish(record: LogRecord?) {
        val level = record?.level?.jlogLevel ?: return
        logger.log(
            LogData(
                record.loggerName,
                level,
                record.message,
                record.thrown,
                record.parameters?.withIndex()?.associate { it.index.toString() to it.value } ?: emptyMap(),
                LogContext(
                    mapOf(
                        "threadId" to record.threadID,
                        "sourceClassName" to record.sourceClassName,
                        "sourceMethodName" to record.sourceMethodName,
                        "sequenceNumber" to record.sequenceNumber,
                    ),
                ),
                Instant.fromEpochMilliseconds(record.instant.toEpochMilli())
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
            )
        )
    }

    override fun flush() {
        //do nothing
    }

    override fun close() {
        //do nothing
    }
}