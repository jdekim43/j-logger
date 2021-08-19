package kr.jadekim.logger.option

import kr.jadekim.logger.LogLevel
import kr.jadekim.logger.pipeline.JLogPipe

data class JLoggerOption(
    val level: LogLevel,
    val pipeline: List<JLogPipe>,
)