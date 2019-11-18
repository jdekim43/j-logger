package kr.jadekim.logger.processor

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import kr.jadekim.logger.interceptor.LogInterceptor
import kr.jadekim.logger.model.Log
import kr.jadekim.logger.printer.LogPrinter
import kotlin.coroutines.CoroutineContext

class AsyncLoggingProcessor(
    interceptors: List<LogInterceptor>,
    printers: List<LogPrinter>,
    val capacity: Int = UNLIMITED
) : LoggingProcessor(interceptors, printers), CoroutineScope {

    override val coroutineContext: CoroutineContext = CoroutineName("JLogger-Processor")

    private val logQueue = Channel<Log>(capacity)

    init {
        launch {
            for (log in logQueue) {
                print(log)
            }
        }
    }

    override fun log(log: Log) {
        logQueue.offer(log)
    }
}