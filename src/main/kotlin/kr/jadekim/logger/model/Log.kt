package kr.jadekim.logger.model

data class Log(
    val loggerName: String,
    val level: Level,
    val message: String,
    val throwable: Throwable?,
    val extra: Map<String, Any>,
    val context: Map<String, Any> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis(),
    val thread: String = Thread.currentThread().name
) {

    fun isPrintable(level: Level) = this.level.isPrintable(level)
}