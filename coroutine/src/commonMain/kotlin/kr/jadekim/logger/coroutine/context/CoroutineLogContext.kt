package kr.jadekim.logger.coroutine.context

import kr.jadekim.logger.context.MutableLogContext
import kr.jadekim.logger.context.ThreadLogContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class CoroutineLogContext(
    data: MutableLogContext = MutableLogContext(),
) : AbstractCoroutineContextElement(Key), MutableLogContext by data {

    constructor(data: Map<String, Any?>) : this(MutableLogContext(data.toMutableMap()))

    companion object Key : CoroutineContext.Key<CoroutineLogContext> {

        @JvmStatic
        suspend fun get(): CoroutineLogContext =
            coroutineContext[CoroutineLogContext] ?: CoroutineLogContext(ThreadLogContext)
    }
}