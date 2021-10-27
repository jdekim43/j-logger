package kr.jadekim.logger.coroutine

import kr.jadekim.logger.JLogger
import kr.jadekim.logger.LogExtra
import kr.jadekim.logger.LogLevel
import kr.jadekim.logger.context.LogContext
import kr.jadekim.logger.coroutine.context.CoroutineLogContext
import kotlin.coroutines.coroutineContext

suspend fun JLogger.sLog(
    level: LogLevel,
    message: String,
    throwable: Throwable? = null,
    meta: Map<String, Any?> = emptyMap(),
    context: LogContext? = null,
) {
    val coroutineLogContext = coroutineContext[CoroutineLogContext]?.snap()
    val logContext = if (coroutineLogContext == null) {
        context
    } else {
        coroutineLogContext + context
    }

    log(level, message, throwable, meta, logContext)
}

suspend fun JLogger.sLog(level: LogLevel, log: LogExtra.() -> String) {
    if (!level.isPrintable(this.level)) {
        return
    }

    val extra = LogExtra()
    val message = log(extra)

    sLog(level, message, extra.throwable, extra.meta, extra.context)
}

suspend fun JLogger.sFetal(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
    sLog(LogLevel.FETAL, message, throwable, meta)
}

suspend fun JLogger.sFetal(body: LogExtra.() -> String) {
    sLog(LogLevel.FETAL, body)
}

suspend fun JLogger.sError(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
    sLog(LogLevel.ERROR, message, throwable, meta)
}

suspend fun JLogger.sError(body: LogExtra.() -> String) {
    sLog(LogLevel.ERROR, body)
}

suspend fun JLogger.sWarning(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
    sLog(LogLevel.WARNING, message, throwable, meta)
}

suspend fun JLogger.sWarning(body: LogExtra.() -> String) {
    sLog(LogLevel.WARNING, body)
}

suspend fun JLogger.sInfo(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
    sLog(LogLevel.INFO, message, throwable, meta)
}

suspend fun JLogger.sInfo(body: LogExtra.() -> String) {
    sLog(LogLevel.INFO, body)
}

suspend fun JLogger.sDebug(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
    sLog(LogLevel.DEBUG, message, throwable, meta)
}

suspend fun JLogger.sDebug(body: LogExtra.() -> String) {
    sLog(LogLevel.DEBUG, body)
}

suspend fun JLogger.sTrace(message: String, throwable: Throwable? = null, meta: Map<String, Any?> = emptyMap()) {
    sLog(LogLevel.TRACE, message, throwable, meta)
}

suspend fun JLogger.sTrace(body: LogExtra.() -> String) {
    sLog(LogLevel.TRACE, body)
}
