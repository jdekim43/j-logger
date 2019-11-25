package kr.jadekim.logger.context

import kotlin.concurrent.getOrSet
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

interface LogContext {

    fun set(data: MutableMap<String, Any>)

    fun get(): MutableMap<String, Any>

    fun remove(key: String)

    fun clear()

    operator fun set(key: String, value: Any)

    operator fun get(key: String): Any?
}

object EmptyLogContext : LogContext {

    override fun set(data: MutableMap<String, Any>) {
        throw IllegalStateException("Can't set log context in EmptyLogContext")
    }

    override fun get(): MutableMap<String, Any> = mutableMapOf()

    override fun remove(key: String) {
        throw IllegalStateException("Can't remove log context in EmptyLogContext")
    }

    override fun clear() {
        throw IllegalStateException("Can't clear log context in EmptyLogContext")
    }

    override fun set(key: String, value: Any) {
        throw IllegalStateException("Can't set log context in EmptyLogContext")
    }

    override fun get(key: String): Any? = null
}

object ThreadLogContext : LogContext {

    private val threadLocal = ThreadLocal<MutableMap<String, Any>>()

    override fun set(data: MutableMap<String, Any>) {
        threadLocal.set(data)
    }

    override fun get() = threadLocal.getOrSet { mutableMapOf() }

    override fun remove(key: String) {
        threadLocal.get()?.remove(key)
    }

    override fun clear() = threadLocal.remove()

    override operator fun set(key: String, value: Any) {
        get()[key] = value
    }

    override operator fun get(key: String) = get()[key]
}

data class CoroutineLogContext(
    private var data: MutableMap<String, Any> = mutableMapOf()
) : AbstractCoroutineContextElement(Key), LogContext {

    companion object Key : CoroutineContext.Key<CoroutineLogContext>

    override fun set(data: MutableMap<String, Any>) {
        this.data = data
    }

    override fun get(): MutableMap<String, Any> = data

    override fun remove(key: String) {
        data.remove(key)
    }

    override fun clear() {
        data.clear()
    }

    override fun set(key: String, value: Any) {
        data[key] = value
    }

    override fun get(key: String): Any? = data[key]
}