package kr.jadekim.logger.integration

import com.github.kittinunf.fuel.core.*
import kr.jadekim.logger.JLog
import kr.jadekim.logger.model.Level

class FuelLogger(
        val clientName: String,
        var logLevel: Level = Level.TRACE,
        loggerName: String = "HttpClientLogger-$clientName"
) {

    private val logger = JLog.get(loggerName)

    val requestInterceptor = LogRequestInterceptor {
        log(it.toString())
    }

    val responseInterceptor = LogResponseInterceptor { _, response ->
        log(response.toString())
    }

    fun log(message: String) {
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
}


class LogRequestInterceptor internal constructor(private val log: (Request) -> Unit) : FoldableRequestInterceptor {

    override fun invoke(next: RequestTransformer): RequestTransformer = {
        log(it)
        next(it)
    }
}

class LogResponseInterceptor internal constructor(private val log: (Request, Response) -> Unit) : FoldableResponseInterceptor {

    override fun invoke(next: ResponseTransformer): ResponseTransformer = { request, response ->
        log(request, response)
        next(request, response)
    }
}