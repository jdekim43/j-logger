package kr.jadekim.logger.integration.ktor

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.withContext
import kr.jadekim.logger.context.MutableLogContext
import kr.jadekim.logger.coroutine.context.CoroutineLogContext

class JLogContext private constructor(
    private val context: ApplicationCall.(MutableLogContext) -> Unit = {},
) {

    class Configuration {
        var context: ApplicationCall.(MutableLogContext) -> Unit = {}
    }

    companion object Feature : BaseApplicationPlugin<Application, Configuration, JLogContext> {

        override val key: AttributeKey<JLogContext> = AttributeKey("JLogContext")

        internal val ATTRIBUTE_ROUTE = AttributeKey<Route>("JLogContext.route")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): JLogContext {
            val configuration = Configuration().apply(configure)
            val feature = JLogContext(configuration.context)

            pipeline.environment.monitor.subscribe(Routing.RoutingCallStarted) {
                it.attributes.put(ATTRIBUTE_ROUTE, it.route)
            }

            pipeline.intercept(ApplicationCallPipeline.Setup) {
                val logContext = CoroutineLogContext.get()

                logContext["remoteAddress"] = context.request.host()
                logContext["userAgent"] = context.request.userAgent()
                logContext["headers"] = context.request.headers.toMap()
                logContext["method"] = context.request.httpMethod
                logContext["path"] = context.request.path()

                feature.context(context, logContext)

                withContext(logContext) {
                    proceed()
                }
            }

            pipeline.intercept(ApplicationCallPipeline.Call) {
                val logContext = coroutineContext[CoroutineLogContext] ?: return@intercept
                val route = context.attributes.getOrNull(ATTRIBUTE_ROUTE)

                logContext["route"] = route?.toString()

                try {
                    proceed()
                } finally {
                    logContext["status"] = context.response.status()?.value?.toString()

                    if (route == null) {
                        logContext["route"] = context.attributes.getOrNull(ATTRIBUTE_ROUTE)?.toString()
                    }
                }
            }

            return feature
        }
    }
}