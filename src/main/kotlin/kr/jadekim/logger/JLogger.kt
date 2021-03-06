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
    private val processor: LoggingProcessor
) {

    fun log(
        level: Level,
        message: String,
        throwable: Throwable? = null,
        meta: Map<String, Any?> = emptyMap()
    ) {
        if (level.isPrintable(this.level)) {
            processor.log(
                Log(
                    name,
                    level,
                    message,
                    throwable,
                    meta,
                    GlobalLogContext.get() + ThreadLogContext.get()
                )
            )
        }
    }

    fun log(level: Level, body: LogExtra.() -> String) {
        if (!level.isPrintable(this.level)) {
            return
        }

        val meta = LogExtra()
        val message = body(meta)

        log(level, message, meta.throwable, meta.meta)
    }

    fun error(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        log(Level.ERROR, message, throwable, meta)
    }

    fun error(body: LogExtra.() -> String) {
        log(Level.ERROR, body)
    }

    fun warning(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        log(Level.WARNING, message, throwable, meta)
    }

    fun warning(body: LogExtra.() -> String) {
        log(Level.WARNING, body)
    }

    fun info(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        log(Level.INFO, message, throwable, meta)
    }

    fun info(body: LogExtra.() -> String) {
        log(Level.INFO, body)
    }

    fun debug(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        log(Level.DEBUG, message, throwable, meta)
    }

    fun debug(body: LogExtra.() -> String) {
        log(Level.DEBUG, body)
    }

    fun trace(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        log(Level.TRACE, message, throwable, meta)
    }

    fun trace(body: LogExtra.() -> String) {
        log(Level.TRACE, body)
    }

    suspend fun sLog(
        level: Level,
        message: String,
        throwable: Throwable? = null,
        meta: Map<String, Any?> = emptyMap()
    ) {
        if (level.isPrintable(this.level)) {
            processor.log(
                Log(
                    name, level,
                    message,
                    throwable,
                    meta,
                    GlobalLogContext.get() + CoroutineLogContext.get()
                )
            )
        }
    }

    suspend fun sLog(level: Level, body: LogExtra.() -> String) {
        if (!level.isPrintable(this.level)) {
            return
        }

        val meta = LogExtra()
        val message = body(meta)

        sLog(level, message, meta.throwable, meta.meta)
    }

    suspend fun sError(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        sLog(Level.ERROR, message, throwable, meta)
    }

    suspend fun sError(body: LogExtra.() -> String) {
        sLog(Level.ERROR, body)
    }

    suspend fun sWarning(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        sLog(Level.WARNING, message, throwable, meta)
    }

    suspend fun sWarning(body: LogExtra.() -> String) {
        sLog(Level.WARNING, body)
    }

    suspend fun sInfo(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        sLog(Level.INFO, message, throwable, meta)
    }

    suspend fun sInfo(body: LogExtra.() -> String) {
        sLog(Level.INFO, body)
    }

    suspend fun sDebug(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        sLog(Level.DEBUG, message, throwable, meta)
    }

    suspend fun sDebug(body: LogExtra.() -> String) {
        sLog(Level.DEBUG, body)
    }

    suspend fun sTrace(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        sLog(Level.TRACE, message, throwable, meta)
    }

    suspend fun sTrace(body: LogExtra.() -> String) {
        sLog(Level.TRACE, body)
    }
}

class LogExtra {
    var throwable: Throwable? = null
    var meta: Map<String, Any?> = emptyMap()
}