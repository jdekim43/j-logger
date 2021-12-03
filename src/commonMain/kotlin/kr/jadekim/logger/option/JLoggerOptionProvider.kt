package kr.jadekim.logger.option

import kr.jadekim.logger.JLog

interface JLoggerOptionProvider {

    companion object {
        fun builder() = JLoggerOptionProviderBuilder()
    }

    operator fun get(loggerName: String): JLoggerOption
}

class JLoggerOptionProviderImpl(
    private val exactly: Map<String, JLoggerOption>,
    private val prefix: List<Pair<String, JLoggerOption>>,
) : JLoggerOptionProvider {

    override operator fun get(loggerName: String): JLoggerOption = exactly[loggerName]
        ?: prefix.firstOrNull { loggerName.startsWith(it.first) }?.second
        ?: JLoggerOption(JLog.loggerLevel, JLog.pipeline)
}

class JLoggerOptionProviderBuilder {

    private val exactly: MutableMap<String, JLoggerOption> = mutableMapOf()
    private val prefix: MutableList<Pair<String, JLoggerOption>> = mutableListOf()

    fun exactly(loggerName: String, option: JLoggerOption): JLoggerOptionProviderBuilder {
        exactly[loggerName] = option

        return this
    }

    fun prefix(loggerName: String, option: JLoggerOption): JLoggerOptionProviderBuilder {
        prefix.add(loggerName to option)
        prefix.sortByDescending { it.first }

        return this
    }

    fun build(): JLoggerOptionProvider = JLoggerOptionProviderImpl(exactly, prefix)
}
