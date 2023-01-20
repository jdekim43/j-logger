package kr.jadekim.logger

import kr.jadekim.logger.context.GlobalLogContext
import kr.jadekim.logger.context.LogContext
import kr.jadekim.logger.context.ThreadLogContext
import kr.jadekim.logger.pipeline.JLogPipe
import kr.jadekim.logger.pipeline.handle

@Suppress("unused")
open class JLogger(
    val name: String,
    var level: LogLevel,
    private val pipeline: List<JLogPipe>,
) {

    open fun log(log: Log) {
        if (log.isPrintable(level)) {
            pipeline.handle(log)
        }
    }

    fun log(
        level: LogLevel,
        message: String,
        throwable: Throwable? = null,
        meta: Map<String, Any?> = emptyMap(),
        context: LogContext? = null,
    ) {
        val logContext = GlobalLogContext.snap() + ThreadLogContext.snap() + context

        log(LogData(name, level, message, throwable, meta, logContext))
    }

    fun log(level: LogLevel, log: LogExtra.() -> String) {
        if (!level.isPrintableAt(this.level)) {
            return
        }

        val extra = LogExtra()
        val message = log(extra)

        log(level, message, extra.throwable, extra.meta, extra.context)
    }

    fun fetal(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        log(LogLevel.FETAL, message, throwable, meta)
    }

    fun fetal(body: LogExtra.() -> String) {
        log(LogLevel.FETAL, body)
    }

    fun error(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        log(LogLevel.ERROR, message, throwable, meta)
    }

    fun error(body: LogExtra.() -> String) {
        log(LogLevel.ERROR, body)
    }

    fun warning(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        log(LogLevel.WARNING, message, throwable, meta)
    }

    fun warning(body: LogExtra.() -> String) {
        log(LogLevel.WARNING, body)
    }

    fun info(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        log(LogLevel.INFO, message, throwable, meta)
    }

    fun info(body: LogExtra.() -> String) {
        log(LogLevel.INFO, body)
    }

    fun debug(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        log(LogLevel.DEBUG, message, throwable, meta)
    }

    fun debug(body: LogExtra.() -> String) {
        log(LogLevel.DEBUG, body)
    }

    fun trace(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
        log(LogLevel.TRACE, message, throwable, meta)
    }

    fun trace(body: LogExtra.() -> String) {
        log(LogLevel.TRACE, body)
    }
}

class LogExtra {
    var throwable: Throwable? = null
    var meta: Map<String, Any?> = emptyMap()
    var context: LogContext? = null
}