package kr.jadekim.logger.integration

import kr.jadekim.logger.JLog
import org.koin.core.Koin
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

class KoinLogger : Logger(Level.DEBUG) {

    private val logger = JLog.get(Koin::class)

    override fun log(level: Level, msg: MESSAGE) {
        when (level) {
            Level.DEBUG -> logger.debug(msg)
            Level.INFO -> logger.info(msg)
            Level.ERROR -> logger.error(msg)
            Level.NONE -> {
                //do nothing
            }
        }
    }
}