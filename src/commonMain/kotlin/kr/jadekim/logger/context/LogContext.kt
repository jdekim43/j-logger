package kr.jadekim.logger.context

import co.touchlab.stately.collections.SharedHashMap
import co.touchlab.stately.concurrency.ThreadLocalRef

interface LogContext : Map<String, Any?> {

    operator fun plus(other: LogContext?): LogContext

    fun clone(): LogContext = LogContext(toMap())

    fun toImmutable(): LogContext = LogContext(toMap())

    fun toMutable(): MutableLogContext = MutableLogContext(toMutableMap())
}

internal class LogContextImpl(data: Map<String, Any?>) : LogContext, Map<String, Any?> by data {

    override fun plus(other: LogContext?): LogContext = if (other == null) this else {
        LogContext((this as Map<String, Any?>).plus(other))
    }
}

fun LogContext(data: Map<String, Any?>): LogContext = LogContextImpl(data)

object EmptyLogContext : LogContext by LogContext(emptyMap())

interface MutableLogContext : LogContext, MutableMap<String, Any?> {

    override operator fun plus(other: LogContext?): MutableLogContext = if (other == null) clone() else {
        val data = mutableMapOf<String, Any?>()

        data.putAll(this)
        data.putAll(other)

        MutableLogContext(data)
    }

    operator fun plusAssign(other: LogContext?) {
        if (other != null) {
            putAll(other)
        }
    }

    fun snap() = LogContext(toMap())

    override fun clone(): MutableLogContext = MutableLogContext(toMutableMap())
}

internal class MutableLogContextImpl(
    data: MutableMap<String, Any?>,
) : MutableLogContext,
    MutableMap<String, Any?> by SharedHashMap() {

    init {
        putAll(data)
    }
}

fun MutableLogContext(data: MutableMap<String, Any?> = mutableMapOf()): MutableLogContext = MutableLogContextImpl(data)

object GlobalLogContext : MutableLogContext by MutableLogContext()

object ThreadLogContext : MutableLogContext {

    private val threadLocal = ThreadLocalRef<MutableLogContext>()

    private val data: MutableLogContext
        get() {
            var data: MutableLogContext? = threadLocal.get()

            if (data == null) {
                data = MutableLogContext()
                threadLocal.set(data)
            }

            return data
        }

    override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
        get() = data.entries

    override val keys: MutableSet<String>
        get() = data.keys

    override val size: Int
        get() = data.size

    override val values: MutableCollection<Any?>
        get() = data.values

    override fun containsKey(key: String): Boolean = data.containsKey(key)

    override fun containsValue(value: Any?): Boolean = data.containsValue(value)

    override fun get(key: String): Any? = data[key]

    override fun isEmpty(): Boolean = data.isEmpty()

    override fun clear() = data.clear()

    override fun put(key: String, value: Any?): Any? = data.put(key, value)

    override fun putAll(from: Map<out String, Any?>) = data.putAll(from)

    override fun remove(key: String): Any? = data.remove(key)
}