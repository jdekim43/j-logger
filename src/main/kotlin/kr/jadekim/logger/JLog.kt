package kr.jadekim.logger

import kr.jadekim.logger.interceptor.ClassLoggerAutoNamer
import kr.jadekim.logger.interceptor.LogInterceptor
import kr.jadekim.logger.model.Level
import kr.jadekim.logger.printer.LogPrinter
import kr.jadekim.logger.processor.AsyncLoggingProcessor
import kr.jadekim.logger.processor.LoggingProcessor
import kotlin.reflect.KProperty

object JLog {

    private val loggerMap = mutableMapOf<String, JLogger>()

    private val interceptors = mutableListOf<LogInterceptor>()
    private val printers = mutableListOf<LogPrinter>()

    private var processor = LoggingProcessor(interceptors, printers)

    fun enableAsync() {
        processor = AsyncLoggingProcessor(interceptors, printers)
    }

    fun enableAsync(capacity: Int) {
        processor = AsyncLoggingProcessor(interceptors, printers, capacity)
    }

    fun addInterceptor(interceptor: LogInterceptor) {
        interceptors.add(interceptor)
    }

    fun autoClassNamer(length: Int = 32) {
        addInterceptor(ClassLoggerAutoNamer(length))
    }

    fun addPrinter(printer: LogPrinter) {
        printers.add(printer)
    }

    fun get(name: String, level: Level = Level.INFO): JLogger {
        return loggerMap.getOrPut(name) { JLogger(name, level, processor) }
    }
}

class CachedLogger<R : Any> {
    private var logger: JLogger? = null

    operator fun getValue(thisRef: R, property: KProperty<*>): JLogger {
        return logger ?: setValue(thisRef, property, JLog.get(thisRef::class.java.canonicalName))
    }

    operator fun setValue(thisRef: R, property: KProperty<*>, value: JLogger): JLogger {
        logger = value

        return value
    }
}

var Any.logger: JLogger by CachedLogger()

fun Any.error(message: String, throwable: Throwable? = null, extra: Map<String, Any> = emptyMap()) {
    logger.log(Level.ERROR, message, throwable, extra)
}

fun Any.warning(message: String, throwable: Throwable? = null, extra: Map<String, Any> = emptyMap()) {
    logger.log(Level.WARNING, message, throwable, extra)
}

fun Any.info(message: String, throwable: Throwable? = null, extra: Map<String, Any> = emptyMap()) {
    logger.log(Level.INFO, message, throwable, extra)
}

fun Any.debug(message: String, throwable: Throwable? = null, extra: Map<String, Any> = emptyMap()) {
    logger.log(Level.DEBUG, message, throwable, extra)
}

fun Any.trace(message: String, throwable: Throwable? = null, extra: Map<String, Any> = emptyMap()) {
    logger.log(Level.TRACE, message, throwable, extra)
}