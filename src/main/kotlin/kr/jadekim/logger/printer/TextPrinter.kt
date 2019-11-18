package kr.jadekim.logger.printer

import kr.jadekim.logger.formatter.DefaultLogFormatter
import kr.jadekim.logger.formatter.LogFormatter
import kr.jadekim.logger.model.Log
import java.io.OutputStream
import java.io.PrintWriter

class TextPrinter(
    val formatter: LogFormatter = DefaultLogFormatter(),
    output: OutputStream = System.out,
    override var printStackTrace: Boolean = true
) : LogPrinter {

    private val writer = PrintWriter(output)

    override fun print(log: Log) {
        writer.write(formatter.format(log))
        if (printStackTrace) {
            log.throwable?.printStackTrace(writer)
        }
        writer.flush()
    }
}