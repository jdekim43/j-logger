package kr.jadekim.logger

import kr.jadekim.logger.context.CoroutineLogContext
import kr.jadekim.logger.context.GlobalLogContext
import kr.jadekim.logger.context.ThreadLogContext
import kr.jadekim.logger.model.Level
import kr.jadekim.logger.model.Log
import kr.jadekim.logger.processor.LoggingProcessor

class JLogger(
    val name: String,
    var level: Level,
    private val processorProvider: () -> LoggingProcessor
) {

    fun log(
        level: Level,
        message: String,
        throwable: Throwable? = null,
        extra: Map<String, Any?> = emptyMap()
    ) {
        if (this.level.isPrintable(level)) {
            processorProvider().log(
                Log(
                    name,
                    level,
                    message,
                    throwable,
                    extra,
                    GlobalLogContext.get() + ThreadLogContext.get()
                )
            )
        }
    }

    fun error(message: String, throwable: Throwable? = null, extra: Map<String, Any?> = emptyMap()) {
        log(Level.ERROR, message, throwable, extra)
    }

    fun warning(message: String, throwable: Throwable? = null, extra: Map<String, Any?> = emptyMap()) {
        log(Level.WARNING, message, throwable, extra)
    }

    fun info(message: String, throwable: Throwable? = null, extra: Map<String, Any?> = emptyMap()) {
        log(Level.INFO, message, throwable, extra)
    }

    fun debug(message: String, throwable: Throwable? = null, extra: Map<String, Any?> = emptyMap()) {
        log(Level.DEBUG, message, throwable, extra)
    }

    fun trace(message: String, throwable: Throwable? = null, extra: Map<String, Any?> = emptyMap()) {
        log(Level.TRACE, message, throwable, extra)
    }

    suspend fun sLog(
        level: Level,
        message: String,
        throwable: Throwable? = null,
        extra: Map<String, Any?> = emptyMap()
    ) {
        if (this.level.isPrintable(level)) {
            processorProvider().log(
                Log(
                    name, level,
                    message,
                    throwable,
                    extra,
                    GlobalLogContext.get() + CoroutineLogContext.get()
                )
            )
        }
    }

    suspend fun sError(message: String, throwable: Throwable? = null, extra: Map<String, Any?> = emptyMap()) {
        sLog(Level.ERROR, message, throwable, extra)
    }

    suspend fun sWarning(message: String, throwable: Throwable? = null, extra: Map<String, Any?> = emptyMap()) {
        sLog(Level.WARNING, message, throwable, extra)
    }

    suspend fun sInfo(message: String, throwable: Throwable? = null, extra: Map<String, Any?> = emptyMap()) {
        sLog(Level.INFO, message, throwable, extra)
    }

    suspend fun sDebug(message: String, throwable: Throwable? = null, extra: Map<String, Any?> = emptyMap()) {
        sLog(Level.DEBUG, message, throwable, extra)
    }

    suspend fun sTrace(message: String, throwable: Throwable? = null, extra: Map<String, Any?> = emptyMap()) {
        sLog(Level.TRACE, message, throwable, extra)
    }
}