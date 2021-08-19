package kr.jadekim.logger.integration.fuel

import kr.jadekim.logger.JLog
import kr.jadekim.logger.JLogger
import kr.jadekim.logger.LogLevel

@Deprecated("")
object FuelLogInterceptorFactory {

    fun requestInterceptor(
        clientName: String,
        logLevel: LogLevel = LogLevel.TRACE,
        logger: JLogger = JLog.get("HttpClientLogger-$clientName"),
    ) = FuelLogger(clientName, logLevel, logger).requestInterceptor

    fun responseInterceptor(
        clientName: String,
        logLevel: LogLevel = LogLevel.TRACE,
        logger: JLogger = JLog.get("HttpClientLogger-$clientName"),
    ) = FuelLogger(clientName, logLevel, logger).responseInterceptor

    class FuelLogger(
        val clientName: String,
        var logLevel: LogLevel = LogLevel.TRACE,
        private val logger: JLogger = JLog.get("HttpClientLogger-$clientName"),
    ) {

        val requestInterceptor = FoldableRequestInterceptor {
            log(it.toString())

            it
        }

        val responseInterceptor = FoldableResponseInterceptor { _, response ->
            log(response.toString())

            response
        }

        fun log(message: String) {
            when (logLevel) {
                LogLevel.NONE -> {
                    //do nothing
                }
                LogLevel.FETAL -> logger.fetal(message)
                LogLevel.ERROR -> logger.error(message)
                LogLevel.WARNING -> logger.warning(message)
                LogLevel.INFO -> logger.info(message)
                LogLevel.DEBUG -> logger.debug(message)
                LogLevel.TRACE -> logger.trace(message)
            }
        }
    }
}
