package kr.jadekim.logger.integration.ktor

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.withContext
import kr.jadekim.logger.context.MutableLogContext
import kr.jadekim.logger.coroutine.context.CoroutineLogContext
import kotlin.coroutines.CoroutineContext

class JLogContextConfiguration {
    var setupContext: ApplicationCall.(MutableLogContext) -> Unit = {}
}

private object JLogContextHook : Hook<suspend (ApplicationCall, suspend () -> Unit, CoroutineContext) -> Unit> {

    override fun install(
        pipeline: ApplicationCallPipeline,
        handler: suspend (ApplicationCall, suspend () -> Unit, CoroutineContext) -> Unit
    ) {
        pipeline.intercept(ApplicationCallPipeline.Setup) {
            handler(call, ::proceed, coroutineContext)
        }
    }
}

val JLogContext = createRouteScopedPlugin("JLogContext", { JLogContextConfiguration() }) {
    on(JLogContextHook) { call, proceed, coroutineContext ->
        val logContext = CoroutineLogContext.get()

        logContext["remoteAddress"] = call.request.host()
        logContext["userAgent"] = call.request.userAgent()
        logContext["headers"] = call.request.headers.toMap()
        logContext["method"] = call.request.httpMethod
        logContext["path"] = call.request.path()
        logContext["route"] = when (call) {
            is RoutingCall -> call.route.toString()
            is RoutingPipelineCall -> call.route.toString()
            else -> call.request.path()
        }

        pluginConfig.setupContext(call, logContext)

        withContext(logContext) {
            proceed()
        }
    }
}
