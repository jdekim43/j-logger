package kr.jadekim.logger.integration.slf4j

import kr.jadekim.logger.JLog
import kr.jadekim.logger.JLogger
import kr.jadekim.logger.LogLevel
import org.slf4j.Marker
import org.slf4j.event.Level
import org.slf4j.helpers.LegacyAbstractLogger
import org.slf4j.helpers.MessageFormatter
import org.slf4j.helpers.NormalizedParameters
import org.slf4j.spi.LocationAwareLogger

private val CALLER_NAME = JLoggerAdapter::class.java.name

class JLoggerAdapter(@Transient private val logger: JLogger) : LegacyAbstractLogger(), LocationAwareLogger {

    constructor(name: String) : this(JLog.get(name))

    override fun isTraceEnabled(): Boolean = logger.level.isPrintableAt(LogLevel.TRACE)

    override fun isDebugEnabled(): Boolean = logger.level.isPrintableAt(LogLevel.DEBUG)

    override fun isInfoEnabled(): Boolean = logger.level.isPrintableAt(LogLevel.INFO)

    override fun isWarnEnabled(): Boolean = logger.level.isPrintableAt(LogLevel.WARNING)

    override fun isErrorEnabled(): Boolean = logger.level.isPrintableAt(LogLevel.ERROR)

    override fun handleNormalizedLoggingCall(
        level: Level,
        marker: Marker?,
        messagePattern: String?,
        arguments: Array<out Any>?,
        throwable: Throwable?
    ) {
        val formattedMessage = MessageFormatter.basicArrayFormat(messagePattern, arguments)
        logger.log(level.toJLogLevel(), formattedMessage, throwable)
    }

    override fun getFullyQualifiedCallerName(): String = CALLER_NAME

    override fun log(
        marker: Marker?,
        fqcn: String?,
        level: Int,
        message: String?,
        argArray: Array<out Any>?,
        t: Throwable?,
    ) {
        val jLogLevel = Level.intToLevel(level).toJLogLevel()

        if (logger.level.isPrintableAt(jLogLevel)) {
            val normalizedParameters = NormalizedParameters.normalize(message, argArray, t)
            val formattedMessage =
                MessageFormatter.basicArrayFormat(normalizedParameters.message, normalizedParameters.arguments)

            logger.log(jLogLevel, formattedMessage, normalizedParameters.throwable)
        }
    }

    private fun Level.toJLogLevel(): LogLevel = when (this) {
        Level.ERROR -> LogLevel.ERROR
        Level.WARN -> LogLevel.WARNING
        Level.INFO -> LogLevel.INFO
        Level.DEBUG -> LogLevel.DEBUG
        Level.TRACE -> LogLevel.TRACE
    }
}