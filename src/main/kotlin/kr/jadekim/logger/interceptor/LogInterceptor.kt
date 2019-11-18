package kr.jadekim.logger.interceptor

import kr.jadekim.logger.model.Log

interface LogInterceptor {

    fun filter(log: Log): Boolean = true

    fun convert(log: Log): Log = log
}