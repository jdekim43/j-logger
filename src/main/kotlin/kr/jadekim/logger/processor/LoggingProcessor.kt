package kr.jadekim.logger.processor

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kr.jadekim.logger.interceptor.LogInterceptor
import kr.jadekim.logger.model.Log
import kr.jadekim.logger.printer.LogPrinter
import kotlin.coroutines.CoroutineContext

open class LoggingProcessor(
    private val interceptors: List<LogInterceptor>,
    private val printers: List<LogPrinter>,
    private val asyncPrinters: List<LogPrinter>,
    val capacity: Int = Channel.UNLIMITED
) : CoroutineScope {

    override val coroutineContext: CoroutineContext = CoroutineName("JLogger-Processor")

    private val logQueue = Channel<Log>(capacity)

    init {
        launch {
            for (log in logQueue) {
                asyncPrint(log)
            }
        }
    }

    open fun log(log: Log) {
        var realLog = log

        for (interceptor in interceptors) {
            if (!interceptor.filter(realLog)) {
                return
            }

            realLog = interceptor.convert(log)
        }

        print(realLog)
        logQueue.offer(realLog)
    }

    protected open fun print(log: Log) {
        for (printer in printers) {
            printer.print(log)
        }
    }

    protected open fun asyncPrint(log: Log) {
        for (printer in asyncPrinters) {
            printer.print(log)
        }
    }
}