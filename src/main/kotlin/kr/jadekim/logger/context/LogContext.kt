package kr.jadekim.logger.context

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.concurrent.getOrSet
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

interface LogContext {

    fun set(data: Map<String, Any?>)

    fun get(): Map<String, Any?>

    fun remove(key: String)

    fun clear()

    operator fun set(key: String, value: Any?)

    operator fun get(key: String): Any?
}

object EmptyLogContext : LogContext {

    override fun set(data: Map<String, Any?>) {
        throw IllegalStateException("Can't set log context in EmptyLogContext")
    }

    override fun get(): Map<String, Any?> = mutableMapOf()

    override fun remove(key: String) {
        throw IllegalStateException("Can't remove log context in EmptyLogContext")
    }

    override fun clear() {
        throw IllegalStateException("Can't clear log context in EmptyLogContext")
    }

    override fun set(key: String, value: Any?) {
        throw IllegalStateException("Can't set log context in EmptyLogContext")
    }

    override fun get(key: String): Any? = null
}

object GlobalLogContext : LogContext {

    private var data = mutableMapOf<String, Any?>()

    override fun set(data: Map<String, Any?>) {
        this.data = data.toMutableMap()
    }

    override fun get(): Map<String, Any?> = data

    override fun remove(key: String) {
        data.remove(key)
    }

    override fun clear() {
        data.clear()
    }

    override fun set(key: String, value: Any?) {
        data[key] = value
    }

    override fun get(key: String): Any? = data[key]
}

object ThreadLogContext : LogContext {

    private val threadLocal = ThreadLocal<MutableMap<String, Any?>>()

    override fun set(data: Map<String, Any?>) {
        threadLocal.set(data.toMutableMap())
    }

    override fun get() = threadLocal.getOrSet { mutableMapOf() }

    override fun remove(key: String) {
        threadLocal.get()?.remove(key)
    }

    override fun clear() = threadLocal.remove()

    override operator fun set(key: String, value: Any?) {
        get()[key] = value
    }

    override operator fun get(key: String) = get()[key]
}

class CoroutineLogContext(
        data: Map<String, Any?> = emptyMap()
) : AbstractCoroutineContextElement(Key), LogContext {

    companion object Key : CoroutineContext.Key<CoroutineLogContext> {

        @JvmStatic
        suspend fun get() = coroutineContext[CoroutineLogContext]?.get() ?: ThreadLogContext.get()
    }

    private var data: ConcurrentMap<String, Any?> = ConcurrentHashMap(data)

    override fun set(data: Map<String, Any?>) {
        this.data = ConcurrentHashMap(data.filter { it.value != null })
    }

    override fun get(): Map<String, Any?> = data

    override fun remove(key: String) {
        data.remove(key)
    }

    override fun clear() {
        data.clear()
    }

    override fun set(key: String, value: Any?) {
        if (value == null) {
            return
        }

        data[key] = value
    }

    override fun get(key: String): Any? = data[key]
}