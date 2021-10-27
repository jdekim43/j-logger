package kr.jadekim.logger.integration.slf4j

import kr.jadekim.logger.context.ThreadLogContext
import org.slf4j.spi.MDCAdapter

class MdcAdapterImpl : MDCAdapter {

    override fun clear() = ThreadLogContext.clear()

    override fun getCopyOfContextMap(): MutableMap<String, String> = ThreadLogContext
        .filterValues { it is String }
        .mapValues { it.value.toString() }
        .toMutableMap()

    override fun put(key: String, value: String) {
        ThreadLogContext[key] = value
    }

    override fun setContextMap(contextMap: MutableMap<String, String>) {
        ThreadLogContext.putAll(contextMap.mapValues { it.value }.toMutableMap())
    }

    override fun remove(key: String) {
        ThreadLogContext.remove(key)
    }

    override fun get(key: String): String? = ThreadLogContext[key]?.toString()
}