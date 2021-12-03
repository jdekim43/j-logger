package kr.jadekim.logger.pipeline

import kr.jadekim.logger.Log
import kr.jadekim.logger.LogData

open class LoggerNameShorter(val preferLength: Int = 32) : JLogPipe {

    companion object Key : JLogPipe.Key<LoggerNameShorter>

    override val key = Key

    override fun install(pipeline: MutableList<JLogPipe>, index: Int) {
        if (pipeline.any { LoggerNameShorter::class.isInstance(it) }) {
            return
        }

        super.install(pipeline, index)
    }

    override fun handle(log: Log): Log {
        if (log is LogData) {
            return log.copy(loggerName = transform(log.loggerName))
        }

        return log
    }

    protected fun transform(name: String): String {
        val result = mutableListOf<String>()
        val tokens = name.split(".")

        if (tokens.size < 2) {
            return name
        }

        var diff = name.length - preferLength
        var idx = 0
        while (diff > 0 && tokens.size - 1 > idx) {
            val token = tokens[idx]
            result.add(token[0].toString())
            diff -= token.length - 1
            idx++
        }

        while (tokens.size > idx) {
            result.add(tokens[idx])
            idx++
        }

        return result.joinToString(".")
    }
}