package kr.jadekim.logger

enum class LogLevel {
    NONE,
    FETAL,
    ERROR,
    WARNING,
    INFO,
    DEBUG,
    TRACE;

    fun isPrintable(level: LogLevel) = ordinal <= level.ordinal
}