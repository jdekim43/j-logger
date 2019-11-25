package kr.jadekim.logger.slf4j

import kr.jadekim.logger.JLog
import kr.jadekim.logger.model.Level
import org.slf4j.Logger
import org.slf4j.Marker

class Slf4jLogger(
    private val name: String
) : Logger {

    private val logger = JLog.get(name, Level.TRACE)

    override fun getName(): String = name

    override fun isTraceEnabled(): Boolean = logger.level.isPrintable(Level.TRACE)

    override fun isTraceEnabled(marker: Marker): Boolean = logger.level.isPrintable(Level.TRACE)

    override fun trace(msg: String) = logger.trace(msg)

    override fun trace(format: String, arg: Any?) = logger.trace(format.format(arg))

    override fun trace(format: String, arg1: Any?, arg2: Any?) = logger.trace(format.format(arg1, arg2))

    override fun trace(format: String, vararg arguments: Any?) = logger.trace(format.format(*arguments))

    override fun trace(msg: String, t: Throwable?) = logger.trace(msg, t)

    override fun trace(marker: Marker, msg: String) = logger.trace(msg)

    override fun trace(marker: Marker, format: String, arg: Any?) = logger.trace(format.format(arg))

    override fun trace(marker: Marker, format: String, arg1: Any?, arg2: Any?) = logger.trace(format.format(arg1, arg2))

    override fun trace(marker: Marker, format: String, vararg argArray: Any?) = logger.trace(format.format(*argArray))

    override fun trace(marker: Marker, msg: String, t: Throwable?) = logger.trace(msg, t)

    override fun isDebugEnabled(): Boolean = logger.level.isPrintable(Level.DEBUG)

    override fun isDebugEnabled(marker: Marker): Boolean = logger.level.isPrintable(Level.DEBUG)

    override fun debug(msg: String) = logger.debug(msg)

    override fun debug(format: String, arg: Any?) = logger.debug(format.format(arg))

    override fun debug(format: String, arg1: Any?, arg2: Any?) = logger.debug(format.format(arg1, arg2))

    override fun debug(format: String, vararg arguments: Any?) = logger.debug(format.format(*arguments))

    override fun debug(msg: String, t: Throwable?) = logger.debug(msg, t)

    override fun debug(marker: Marker, msg: String) = logger.debug(msg)

    override fun debug(marker: Marker, format: String, arg: Any?) = logger.debug(format.format(arg))

    override fun debug(marker: Marker, format: String, arg1: Any?, arg2: Any?) = logger.debug(format.format(arg1, arg2))

    override fun debug(marker: Marker, format: String, vararg argArray: Any?) = logger.debug(format.format(*argArray))

    override fun debug(marker: Marker, msg: String, t: Throwable?) = logger.debug(msg, t)

    override fun isInfoEnabled(): Boolean = logger.level.isPrintable(Level.INFO)

    override fun isInfoEnabled(marker: Marker): Boolean = logger.level.isPrintable(Level.INFO)

    override fun info(msg: String) = logger.info(msg)

    override fun info(format: String, arg: Any?) = logger.info(format.format(arg))

    override fun info(format: String, arg1: Any?, arg2: Any?) = logger.info(format.format(arg1, arg2))

    override fun info(format: String, vararg arguments: Any?) = logger.info(format.format(*arguments))

    override fun info(msg: String, t: Throwable?) = logger.info(msg, t)

    override fun info(marker: Marker, msg: String) = logger.info(msg)

    override fun info(marker: Marker, format: String, arg: Any?) = logger.info(format.format(arg))

    override fun info(marker: Marker, format: String, arg1: Any?, arg2: Any?) = logger.info(format.format(arg1, arg2))

    override fun info(marker: Marker, format: String, vararg argArray: Any?) = logger.info(format.format(*argArray))

    override fun info(marker: Marker, msg: String, t: Throwable?) = logger.info(msg, t)

    override fun isWarnEnabled(): Boolean = logger.level.isPrintable(Level.WARNING)

    override fun isWarnEnabled(marker: Marker): Boolean = logger.level.isPrintable(Level.WARNING)

    override fun warn(msg: String) = logger.warning(msg)

    override fun warn(format: String, arg: Any?) = logger.warning(format.format(arg))

    override fun warn(format: String, arg1: Any?, arg2: Any?) = logger.warning(format.format(arg1, arg2))

    override fun warn(format: String, vararg arguments: Any?) = logger.warning(format.format(*arguments))

    override fun warn(msg: String, t: Throwable?) = logger.warning(msg, t)

    override fun warn(marker: Marker, msg: String) = logger.warning(msg)

    override fun warn(marker: Marker, format: String, arg: Any?) = logger.warning(format.format(arg))

    override fun warn(marker: Marker, format: String, arg1: Any?, arg2: Any?) {
        logger.warning(format.format(arg1, arg2))
    }

    override fun warn(marker: Marker, format: String, vararg argArray: Any?) = logger.warning(format.format(*argArray))

    override fun warn(marker: Marker, msg: String, t: Throwable?) = logger.warning(msg, t)

    override fun isErrorEnabled(): Boolean = logger.level.isPrintable(Level.ERROR)

    override fun isErrorEnabled(marker: Marker): Boolean = logger.level.isPrintable(Level.ERROR)

    override fun error(msg: String) = logger.error(msg)

    override fun error(format: String, arg: Any?) = logger.error(format.format(arg))

    override fun error(format: String, arg1: Any?, arg2: Any?) = logger.error(format.format(arg1, arg2))

    override fun error(format: String, vararg arguments: Any?) = logger.error(format.format(*arguments))

    override fun error(msg: String, t: Throwable?) = logger.error(msg, t)

    override fun error(marker: Marker, msg: String) = logger.error(msg)

    override fun error(marker: Marker, format: String, arg: Any?) = logger.error(format.format(arg))

    override fun error(marker: Marker, format: String, arg1: Any?, arg2: Any?) = logger.error(format.format(arg1, arg2))

    override fun error(marker: Marker, format: String, vararg argArray: Any?) = logger.error(format.format(*argArray))

    override fun error(marker: Marker, msg: String, t: Throwable?) = logger.error(msg, t)
}