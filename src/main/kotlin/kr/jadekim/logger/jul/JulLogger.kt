package kr.jadekim.logger.jul

import kr.jadekim.logger.JLog
import kr.jadekim.logger.model.Level
import kr.jadekim.logger.model.Log
import java.util.logging.Handler
import java.util.logging.LogRecord
import java.util.logging.Level as JulLevel

class JulLogger : Handler() {

    override fun publish(record: LogRecord) {
        val level = record.level.toJLogLevel() ?: return
        JLog.processor.log(
            Log(
                record.loggerName,
                level,
                record.message,
                record.thrown,
                record.parameters?.mapIndexed { idx, each -> Pair(idx.toString(), each) }?.toMap() ?: emptyMap(),
                timestamp = record.millis,
                thread = record.threadID.toString()
            )
        )
    }

    override fun flush() {
        //do nothing
    }

    override fun close() {
        //do nothing
    }

    private fun JulLevel.toJLogLevel(): Level? = when (this) {
        JulLevel.SEVERE -> Level.ERROR
        JulLevel.WARNING -> Level.WARNING
        JulLevel.INFO -> Level.INFO
        JulLevel.CONFIG, JulLevel.FINE -> Level.DEBUG
        JulLevel.ALL, JulLevel.FINER, JulLevel.FINEST -> Level.TRACE
        JulLevel.OFF -> null
        else -> null
    }
}