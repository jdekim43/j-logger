package kr.jadekim.logger.formatter

import kr.jadekim.logger.model.Log
import java.text.DateFormat
import java.text.SimpleDateFormat

class DefaultLogFormatter(
    var printMeta: Boolean = true
) : LogFormatter {

    var dateFormatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")

    override fun format(log: Log): String {
        val builder = StringBuilder().apply {
            append("%-23s".format(dateFormatter.format(log.timestamp)))
            append(" %-5s".format(log.level.name))
            append(" %-32s".format(log.loggerName))
            append(" [ %-20s ]".format(log.thread))
            append(" : ${log.message}")
        }

        if (printMeta && log.meta.isNotEmpty()) {
            log.meta.map { "${it.key}=${it.value}" }.joinTo(builder, ", ", prefix = "(", postfix = ")")
        }

        return builder.appendln().toString()
    }
}