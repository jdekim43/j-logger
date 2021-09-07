package kr.jadekim.logger.integration.fuel

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kr.jadekim.logger.JLog
import kr.jadekim.logger.JLogger
import kr.jadekim.logger.LogLevel
import kr.jadekim.logger.context.GlobalLogContext
import kr.jadekim.logger.context.LogContext
import kr.jadekim.logger.template.HttpRequestLog
import kr.jadekim.logger.template.HttpResponseLog


class FuelLogger(
    val clientName: String,
    val option: HttpLogOption,
    private val logger: JLogger = JLog.get("HttpClientLogger-$clientName")
) {

    data class HttpLogOption(
        var successLogLevel: LogLevel = LogLevel.DEBUG,
        var failLogLevel: LogLevel = LogLevel.WARNING,
        var includeRequestHeaders: Boolean = false,
        var includeRequestBody: Boolean = false,
        var includeResponseHeaders: Boolean = false,
        var includeResponseBody: Boolean = false,
        var combineLog: Boolean = true,
    )

    fun requestInterceptor() = FoldableRequestInterceptor { request ->
        val timestamp = Clock.System.now().toLocalDateTime(TimeZone.UTC)

        if (!option.combineLog) {
            val requestLog = request.toRequestLog(timestamp)
            val logContext = GlobalLogContext.snap() + request.getTag(LogContext::class)
            logger.log(requestLog.toLogData(logger.name, option.successLogLevel, logContext))
        }

        request.tag(timestamp)
    }

    fun responseInterceptor() = FoldableResponseInterceptor { request, response ->
        val responseTimestamp = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val requestTimestamp = request.getTag(LocalDateTime::class) ?: responseTimestamp
        val logContext = GlobalLogContext.snap() + request.getTag(LogContext::class)

        val requestLog = request.toRequestLog(requestTimestamp)
        val responseLog = response.toResponseLog(requestLog, responseTimestamp)

        logger.log(responseLog.toLogData(logger.name, option.successLogLevel, logContext, option.combineLog))

        response
    }

    private fun Request.toRequestLog(timestamp: LocalDateTime) = HttpRequestLog(
        "",
        method.name,
        url.protocol,
        url.host,
        url.port,
        url.path,
        url.query,
        if (option.includeRequestHeaders) headers.mapValues { it.value.firstOrNull() } else emptyMap(),
        if (option.includeRequestBody) body.asString(null) else null,
        timestamp = timestamp,
    )

    private fun Response.toResponseLog(requestLog: HttpRequestLog, timestamp: LocalDateTime) = HttpResponseLog(
        requestLog,
        statusCode,
        if (option.includeResponseHeaders) headers.mapValues { it.value.firstOrNull() } else emptyMap(),
        if (option.includeResponseBody) body().asString(null) else null,
        timestamp = timestamp,
    )
}