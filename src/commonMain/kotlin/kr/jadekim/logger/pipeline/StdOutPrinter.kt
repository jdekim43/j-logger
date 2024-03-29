package kr.jadekim.logger.pipeline

import kr.jadekim.logger.Log
import kr.jadekim.logger.LogLevel
import kr.jadekim.logger.SerializedLog

internal expect fun eprintln(text: String)

class StdOutPrinter(
    val printStackTrace: Boolean = true,
    val useStdErr: Boolean = true,
) : JLogPipe {

    companion object Key : JLogPipe.Key<StdOutPrinter>

    override val key = Key

    override fun install(pipeline: MutableList<JLogPipe>, index: Int) {
        if (pipeline.any { StdOutPrinter::class.isInstance(it) }) {
            return
        }

        pipeline.add(index, this)
    }

    override fun handle(log: Log): Log {
        when (log) {
            is SerializedLog.String -> {
                if (log.level.isPrintableAt(LogLevel.ERROR)) {
                    eprintln(log.data)
                } else {
                    println(log.data)
                }
            }
            else -> println("ERROR: StdOutPrinter is only acceptable SerializedLog.String. Require to install TextFormatter")
        }

        if (printStackTrace) {
            log.throwable?.printStackTrace()
        }

        return log
    }
}
