package kr.jadekim.logger.processor

import kr.jadekim.logger.interceptor.LogInterceptor
import kr.jadekim.logger.model.Log
import kr.jadekim.logger.printer.LogPrinter

open class LoggingProcessor(
    private val interceptors: List<LogInterceptor>,
    private val printers: List<LogPrinter>
) {

    open fun log(log: Log) {
        print(log)
    }

    protected open fun print(log: Log) {
        var realLog = log

        for (interceptor in interceptors) {
            if (!interceptor.filter(realLog)) {
                return
            }

            realLog = interceptor.convert(log)
        }

        for (printer in printers) {
            printer.print(realLog)
        }
    }
}