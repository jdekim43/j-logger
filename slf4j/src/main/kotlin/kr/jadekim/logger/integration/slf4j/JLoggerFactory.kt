package kr.jadekim.logger.integration.slf4j

import org.slf4j.ILoggerFactory
import org.slf4j.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class JLoggerFactory : ILoggerFactory {

    private val loggerMap: ConcurrentMap<String, JLoggerAdapter> = ConcurrentHashMap()

    override fun getLogger(name: String): Logger = loggerMap.getOrPut(name) { JLoggerAdapter(name) }
}