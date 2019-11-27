package kr.jadekim.logger.formatter

import kr.jadekim.logger.model.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DefaultLogFormatter : LogFormatter {

    var dateFormatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")

    override fun format(log: Log): String {
        return StringBuilder().apply {
            append("%-23s".format(dateFormatter.format(Date(log.timestamp))))
            append(" %-5s".format(log.level.name))
            append(" %-32s".format(log.loggerName))
            append(" [ %-20s ]".format(log.thread))
            append(" : ${log.message}\n")
        }.toString()
    }
}