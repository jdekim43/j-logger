package kr.jadekim.logger.model

enum class Level {
    ERROR,
    WARNING,
    INFO,
    DEBUG,
    TRACE;

    fun isPrintable(level: Level) = this.ordinal >= level.ordinal
}