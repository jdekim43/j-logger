package kr.jadekim.logger.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Log(
    val loggerName: String,
    val level: Level,
    val message: String,
    val throwable: Throwable?,
    val extra: Map<String, Any?>,
    @JsonProperty("logContext") val context: Map<String, Any?> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis(),
    val thread: String = Thread.currentThread().name
) {

    fun isPrintable(level: Level) = this.level.isPrintable(level)
}