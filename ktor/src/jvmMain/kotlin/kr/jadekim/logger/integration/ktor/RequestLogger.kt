package kr.jadekim.logger.integration.ktor

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
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
import kr.jadekim.logger.integration.ktor.JLogContext.Feature.ATTRIBUTE_ROUTE

val REQUEST_LOG_ENABLE = AttributeKey<Boolean>("requestLog.enable")
val REQUEST_LOG_BODY = AttributeKey<Boolean>("requestLog.printBody")

fun PipelineContext<Unit, ApplicationCall>.disableRequestLog() {
    context.attributes.put(REQUEST_LOG_ENABLE, false)
}

fun PipelineContext<Unit, ApplicationCall>.logWithBody() {
    context.attributes.put(REQUEST_LOG_BODY, true)
}

fun PipelineContext<Unit, ApplicationCall>.logWithoutBody() {
    context.attributes.put(REQUEST_LOG_BODY, false)
}

val HttpStatusCode.defaultLogLevel: LogLevel
    get() = when(value / 100) {
        5 -> LogLevel.ERROR
        4 -> LogLevel.WARNING
        else -> LogLevel.INFO
    }

class RequestLogger private constructor(
    private val logContext: ApplicationCall.(MutableLogContext) -> Unit,
    private val canLogBody: ApplicationCall.() -> Boolean,
    private val logger: JLogger,
    private val logLevel: ApplicationCall.(Throwable?) -> LogLevel,
) {

    class Configuration {
        var logContext: ApplicationCall.(MutableLogContext) -> Unit = {}
        var canLogBody: ApplicationCall.() -> Boolean = { false }
        var logger: JLogger = JLog.get("RequestLogger")
        var logLevel: ApplicationCall.(Throwable?) -> LogLevel = { response.status()?.defaultLogLevel ?: LogLevel.INFO }
    }

    companion object Feature : BaseApplicationPlugin<Application, Configuration, RequestLogger> {

        val phase = PipelinePhase("RequestLog")

        override val key: AttributeKey<RequestLogger> = AttributeKey("RequestLogger")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): RequestLogger {
            val configuration = Configuration().apply(configure)
            val feature = RequestLogger(
                configuration.logContext,
                configuration.canLogBody,
                configuration.logger,
                configuration.logLevel,
            )

            pipeline.insertPhaseAfter(ApplicationCallPipeline.Monitoring, phase)

            pipeline.intercept(phase) {
                val logContext = CoroutineLogContext.get()
                val meta = MutableLogContext()

                val preHandleTime = Clock.System.now()

                logContext["preHandleTime"] = preHandleTime.toLocalDateTime(TimeZone.UTC)
                meta["pathParameter"] = context.parameters.toKeyValueString()
                meta["query"] = context.request.queryParameters.toKeyValueString()

                if (context.request.httpMethod.readableBody
                    && context.request.contentType().readableBody
                    && context.attributes.getOrNull(REQUEST_LOG_BODY) ?: feature.canLogBody(context)
                ) {
                    meta["body"] = context.receiveText()
                }

                feature.logContext(context, logContext)

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

                    if (context.attributes.getOrNull(REQUEST_LOG_ENABLE) != false) {
                        val route = context.attributes.getOrNull(ATTRIBUTE_ROUTE)
                            ?: "${context.request.path()}/(method:${context.request.httpMethod.value}"
                        val status = context.response.status()?.value?.toString()

                        feature.logger.sLog(feature.logLevel(context, exception), "$route - $status", exception, meta)
                    }

                    if (exception != null) {
                        throw exception
                    }
                }
            }

            return feature
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