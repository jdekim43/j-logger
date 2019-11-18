package kr.jadekim.logger

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
        extra: Map<String, Any> = emptyMap(),
        context: Map<String, Any> = emptyMap()
    ) {
        if (this.level.isPrintable(level)) {
            processor.log(Log(name, level, message, throwable, extra, context))
        }
    }

    fun error(message: String, throwable: Throwable? = null, extra: Map<String, Any> = emptyMap()) {
        log(Level.ERROR, message, throwable, extra)
    }

    fun warning(message: String, throwable: Throwable? = null, extra: Map<String, Any> = emptyMap()) {
        log(Level.WARNING, message, throwable, extra)
    }

    fun info(message: String, throwable: Throwable? = null, extra: Map<String, Any> = emptyMap()) {
        log(Level.INFO, message, throwable, extra)
    }

    fun debug(message: String, throwable: Throwable? = null, extra: Map<String, Any> = emptyMap()) {
        log(Level.DEBUG, message, throwable, extra)
    }

    fun trace(message: String, throwable: Throwable? = null, extra: Map<String, Any> = emptyMap()) {
        log(Level.TRACE, message, throwable, extra)
    }
}