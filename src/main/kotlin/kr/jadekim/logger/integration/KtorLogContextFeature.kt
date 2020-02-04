package kr.jadekim.logger.integration

import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelinePhase
import kotlinx.coroutines.withContext
import kr.jadekim.logger.context.CoroutineLogContext

val REQUEST_LOG_CONTEXT = AttributeKey<CoroutineLogContext>("request_log_context")

class KtorLogContextFeature private constructor() {

    class Configuration

    companion object Feature : ApplicationFeature<Application, Configuration, KtorLogContextFeature> {

        override val key: AttributeKey<KtorLogContextFeature> = AttributeKey("KtorLogContextFeature")

        override fun install(
            pipeline: Application,
            configure: Configuration.() -> Unit
        ): KtorLogContextFeature {
            val feature = KtorLogContextFeature()
            val phase = PipelinePhase("JLogIntegration")

            pipeline.insertPhaseBefore(ApplicationCallPipeline.Setup, phase)

            pipeline.intercept(phase) {
                val logContext = context.attributes.getOrNull(REQUEST_LOG_CONTEXT)
                    ?: CoroutineLogContext()

                context.attributes.put(REQUEST_LOG_CONTEXT, logContext)

                withContext(logContext) {
                    proceed()
                }
            }

            return feature
        }
    }
}