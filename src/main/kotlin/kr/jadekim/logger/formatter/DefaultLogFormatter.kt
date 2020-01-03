package kr.jadekim.logger.formatter

import kr.jadekim.logger.model.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DefaultLogFormatter(
    var printExtra: Boolean = true
) : LogFormatter {

    var dateFormatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")

    override fun format(log: Log): String {
        val builder = StringBuilder().apply {
            append("%-23s".format(dateFormatter.format(Date(log.timestamp))))
            append(" %-5s".format(log.level.name))
            append(" %-32s".format(log.loggerName))
            append(" [ %-20s ]".format(log.thread))
            append(" : ${log.message}(")
        }

        log.extra.map { "${it.key}=${it.value}" }.joinTo(builder, ", ")

        return builder.append(")\n").toString()
    }
}