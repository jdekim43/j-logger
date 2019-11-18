package kr.jadekim.logger.formatter

import kr.jadekim.logger.model.Log

interface LogFormatter {

    fun format(log: Log): String
}