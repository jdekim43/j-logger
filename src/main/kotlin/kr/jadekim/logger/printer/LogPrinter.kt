package kr.jadekim.logger.printer

import kr.jadekim.logger.model.Log

interface LogPrinter {

    var printStackTrace: Boolean

    fun print(log: Log)
}