package kr.jadekim.logger.integration.okhttp

import kr.jadekim.logger.JLog
import kr.jadekim.logger.JLogger
import kr.jadekim.logger.LogLevel
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpLogInterceptorFactory {

    class OkHttpLoggerImpl(
        var logLevel: LogLevel,
        private val logger: JLogger,
    ) : HttpLoggingInterceptor.Logger {

        override fun log(message: String) {
            when (logLevel) {
                LogLevel.FETAL -> logger.fetal(message)
                LogLevel.ERROR -> logger.error(message)
                LogLevel.WARNING -> logger.warning(message)
                LogLevel.INFO -> logger.info(message)
                LogLevel.DEBUG -> logger.debug(message)
                LogLevel.TRACE -> logger.trace(message)
                LogLevel.NONE -> {
                    //do nothing
                }
            }
        }
    }

    fun create(
        clientName: String,
        logLevel: LogLevel,
        interceptLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
        logger: JLogger = JLog.get("HttpClientLogger-$clientName"),
    ) = HttpLoggingInterceptor(OkHttpLoggerImpl(logLevel, logger)).apply {
        level = interceptLevel
    }
}