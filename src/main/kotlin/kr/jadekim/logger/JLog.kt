package kr.jadekim.logger

import kr.jadekim.logger.interceptor.ClassLoggerAutoNamer
import kr.jadekim.logger.interceptor.LogInterceptor
import kr.jadekim.logger.model.Level
import kr.jadekim.logger.printer.LogPrinter
import kr.jadekim.logger.processor.LoggingProcessor
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

object JLog {

    var defaultLoggerLevel = Level.TRACE

    private val loggerMap = mutableMapOf<String, JLogger>()

    private val interceptors = mutableListOf<LogInterceptor>()
    private val printers = mutableListOf<LogPrinter>()
    private val asyncPrinters = mutableListOf<LogPrinter>()

    private val exactlyLoggerLevel = mutableMapOf<String, Level>()
    private val prefixLoggerLevel = mutableListOf<Pair<String, Level>>()

    internal val processor = LoggingProcessor(interceptors, printers, asyncPrinters)

    fun addInterceptor(interceptor: LogInterceptor) {
        interceptors.add(interceptor)
    }

    fun autoClassNamer(length: Int = 32) {
        addInterceptor(ClassLoggerAutoNamer(length))
    }

    fun addPrinter(printer: LogPrinter) {
        printers.add(printer)
    }

    fun addAsyncPrinter(printer: LogPrinter) {
        asyncPrinters.add(printer)
    }

    fun exactly(loggerName: String, level: Level) {
        exactlyLoggerLevel[loggerName] = level
    }

    fun prefix(loggerName: String, level: Level) {
        prefixLoggerLevel.add(loggerName to level)
        prefixLoggerLevel.sortByDescending { it.first }
    }

    fun get(name: String): JLogger {
        return loggerMap.getOrPut(name) { JLogger(name, getDefaultLevel(name), processor) }
    }

    fun get(clazz: KClass<*>) = get(clazz.qualifiedName ?: clazz.jvmName)

    fun get(clazz: Class<*>) = get(clazz.canonicalName)

    private fun getDefaultLevel(loggerName: String): Level {
        return exactlyLoggerLevel[loggerName]
            ?: prefixLoggerLevel.firstOrNull { loggerName.startsWith(it.first) }?.second
            ?: defaultLoggerLevel
    }
}