package kr.jadekim.logger.integration.ktor

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kr.jadekim.logger.JLog
import kr.jadekim.logger.JLogger
import kr.jadekim.logger.LogLevel
import kr.jadekim.logger.context.MutableLogContext
import kr.jadekim.logger.coroutine.context.CoroutineLogContext
import kr.jadekim.logger.coroutine.sLog
import kotlin.coroutines.CoroutineContext

val REQUEST_LOG_ENABLE = AttributeKey<Boolean>("requestLog.enable")
val REQUEST_LOG_BODY = AttributeKey<Boolean>("requestLog.printBody")

fun RoutingContext.disableRequestLog() {
    call.attributes.put(REQUEST_LOG_ENABLE, false)
}

fun RoutingContext.logWithBody() {
    call.attributes.put(REQUEST_LOG_BODY, true)
}

fun RoutingContext.logWithoutBody() {
    call.attributes.put(REQUEST_LOG_BODY, false)
}

val HttpStatusCode.defaultLogLevel: LogLevel
    get() = when (value / 100) {
        5 -> LogLevel.ERROR
        4 -> LogLevel.WARNING
        else -> LogLevel.INFO
    }

class RequestLoggerConfiguration {
    var logContext: ApplicationCall.(MutableLogContext) -> Unit = {}
    var canLogBody: ApplicationCall.() -> Boolean = { false }
    var logger: JLogger = JLog.get("RequestLogger")
    var logLevel: ApplicationCall.(Throwable?) -> LogLevel = { response.status()?.defaultLogLevel ?: LogLevel.INFO }
}

private object RequestLoggerHook : Hook<suspend (ApplicationCall, suspend () -> Unit, CoroutineContext) -> Unit> {

    val phase = PipelinePhase("RequestLog")

    override fun install(
        pipeline: ApplicationCallPipeline,
        handler: suspend (ApplicationCall, suspend () -> Unit, CoroutineContext) -> Unit
    ) {
        pipeline.insertPhaseAfter(ApplicationCallPipeline.Monitoring, phase)
        pipeline.intercept(phase) {
            handler(call, ::proceed, coroutineContext)
        }
    }
}

val RequestLogger = createRouteScopedPlugin("RequestLogger", { RequestLoggerConfiguration() }) {

    on(RequestLoggerHook) { call, proceed, coroutineContext ->
        val logContext = CoroutineLogContext.get()
        val meta = MutableLogContext()

        val preHandleTime = Clock.System.now()

        logContext["preHandleTime"] = preHandleTime.toLocalDateTime(TimeZone.UTC)
        meta["pathParameter"] = call.parameters.toKeyValueString()
        meta["query"] = call.request.queryParameters.toKeyValueString()

        if (call.request.httpMethod.readableBody
            && call.request.contentType().readableBody
            && call.attributes.getOrNull(REQUEST_LOG_BODY) ?: pluginConfig.canLogBody(call)
        ) {
            meta["body"] = call.receiveText()
        }

        pluginConfig.logContext(call, logContext)

        withContext(logContext) {
            var exception: Exception? = null
            try {
                proceed()
            } catch (e: Exception) {
                exception = e
            }

            val postHandleTime = Clock.System.now()

            logContext["postHandleTime"] = postHandleTime.toLocalDateTime(TimeZone.UTC)
            logContext["durationToHandle"] = postHandleTime.toEpochMilliseconds() - preHandleTime.toEpochMilliseconds()

            val route = when (call) {
                is RoutingCall -> call.route.toString()
                is RoutingPipelineCall -> call.route.toString()
                else -> "${call.request.path()}/(method:${call.request.httpMethod.value})"
            }
            val status = call.response.status()?.value?.toString()

            if (call.attributes.getOrNull(REQUEST_LOG_ENABLE) != false) {
                pluginConfig.logger.sLog(pluginConfig.logLevel(call, exception), "$route - $status", exception, meta)
            }

            if (exception != null) {
                throw exception
            }
        }
    }
}

private fun Parameters.toKeyValueString() = flattenEntries().joinToString { "${it.first} = ${it.second}" }

private val HttpMethod.readableBody
    get() = when (this) {
        HttpMethod.Post, HttpMethod.Put, HttpMethod.Patch -> true
        else -> false
    }

private val ContentType.readableBody
    get() = when (this) {
        ContentType.Application.Json, ContentType.Application.FormUrlEncoded -> true
        else -> false
    }