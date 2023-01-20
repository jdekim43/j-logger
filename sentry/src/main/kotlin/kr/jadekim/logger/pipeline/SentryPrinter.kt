package kr.jadekim.logger.pipeline

import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import io.sentry.protocol.Message
import kr.jadekim.logger.Log
import kr.jadekim.logger.LogLevel

class SentryPrinter : JLogPipe {

    companion object : JLogPipe.Key<SentryPrinter>

    override val key: JLogPipe.Key<out JLogPipe> = SentryPrinter

    override fun handle(log: Log): Log {
        val sentryLevel = log.level.toSentryLevel()
        if (sentryLevel < SentryLevel.WARNING) {
            return log
        }

        val event = SentryEvent().apply {
            logger = log.loggerName
            level = sentryLevel
            message = Message().apply {
                message = log.message
                params = log.meta.map { "${it.key} - ${it.value}" }
            }
            throwable = log.throwable
            contexts.putAll(log.context)
            log.meta.forEach { (key, value) -> if (value != null) setExtra(key, value) }
        }

        Sentry.captureEvent(event)
        return log
    }

    private fun LogLevel.toSentryLevel(): SentryLevel =
        when (this) {
            LogLevel.DEBUG -> SentryLevel.DEBUG
            LogLevel.WARNING -> SentryLevel.WARNING
            LogLevel.TRACE -> SentryLevel.INFO
            LogLevel.NONE -> SentryLevel.INFO
            LogLevel.INFO -> SentryLevel.INFO
            LogLevel.ERROR -> SentryLevel.ERROR
            LogLevel.FETAL -> SentryLevel.FATAL
        }
}