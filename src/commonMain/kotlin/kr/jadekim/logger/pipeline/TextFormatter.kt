package kr.jadekim.logger.pipeline

import kr.jadekim.logger.Log
import kr.jadekim.logger.SerializedLog

open class TextFormatter(
    var printMeta: Boolean = true,
) : JLogPipe {

    companion object Key : JLogPipe.Key<TextFormatter>

    override val key = Key

    override fun handle(log: Log): SerializedLog.String {
        val text = buildString {
            append(log.timestamp.toString().padEnd(23))
            append(' ')
            append(log.level.name.padEnd(5))
            append(' ')
            append(log.loggerName.padEnd(32))
            append(" : ")
            append(log.message)

            if (printMeta && log.meta.isNotEmpty()) {
                log.meta.map { "${it.key}=${it.value}" }.joinTo(this, ", ", prefix = "(", postfix = ")")
            }
        }

        return SerializedLog.String(log, text)
    }
}