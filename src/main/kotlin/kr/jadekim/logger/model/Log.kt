package kr.jadekim.logger.model

import java.util.Date

data class Log(
    val loggerName: String,
    val level: Level,
    val message: String,
    val throwable: Throwable?,
    val meta: Map<String, Any?>,
    val logContext: Map<String, Any?> = emptyMap(),
    val timestamp: Date = Date(),
    val thread: String = Thread.currentThread().name
) {

    fun isPrintable(level: Level) = this.level.isPrintable(level)
}