package kr.jadekim.logger.pipeline

import kr.jadekim.logger.Log

class StdOutPrinter(
    val printStackTrace: Boolean = true,
) : JLogPipe {

    companion object Key : JLogPipe.Key<StdOutPrinter>

    override val key = Key

    override fun install(pipeline: MutableList<JLogPipe>, index: Int) {
        if (pipeline.any { StdOutPrinter::class.isInstance(it) }) {
            return
        }

        pipeline.add(index, this)

        if (!pipeline.contains(TextFormatter)) {
            pipeline.add(index, TextFormatter())
        }
    }

    override fun handle(log: Log): Log {
        if (log is TextFormatter.FormattedLog) {
            println(log.formattedText)
            log.throwable?.printStackTrace()
        }

        return log
    }
}