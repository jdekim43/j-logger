package kr.jadekim.logger.integration

import kr.jadekim.logger.JLog
import kr.jadekim.logger.model.Level
import okhttp3.logging.HttpLoggingInterceptor

class OkHttpLogger(
    val clientName: String,
    var logLevel: Level = Level.TRACE,
    loggerName: String = "HttpClientLogger-$clientName"
) : HttpLoggingInterceptor.Logger {

    private val logger = JLog.get(loggerName)

    override fun log(message: String) {
        when (logLevel) {
            Level.ERROR -> logger.error(message)
            Level.WARNING -> logger.warning(message)
            Level.INFO -> logger.info(message)
            Level.DEBUG -> logger.debug(message)
            Level.TRACE -> logger.trace(message)
            Level.NONE -> {
                //do nothing
            }
        }
    }

    fun asInterceptor() = HttpLoggingInterceptor(this).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}