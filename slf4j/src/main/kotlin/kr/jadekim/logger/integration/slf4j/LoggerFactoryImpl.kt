package kr.jadekim.logger.integration.slf4j

import org.slf4j.ILoggerFactory
import org.slf4j.Logger

class LoggerFactoryImpl : ILoggerFactory {

    override fun getLogger(name: String): Logger = Slf4jLogger(name)
}