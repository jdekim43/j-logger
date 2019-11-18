package kr.jadekim.logger.interceptor

import kr.jadekim.logger.model.Log

class ClassLoggerAutoNamer(
    val length: Int = 32
) : LogInterceptor {

    override fun convert(log: Log): Log {
        return log.copy(loggerName = transform(log.loggerName))
    }

    private fun transform(name: String): String {
        val result = mutableListOf<String>()
        val tokens = name.split(".")

        if (tokens.size < 2) {
            return name
        }

        var diff = name.length - length
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