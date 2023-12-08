package kr.jadekim.logger.coroutine.context

import kr.jadekim.logger.context.MutableLogContext
import kr.jadekim.logger.context.ThreadLogContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.jvm.JvmStatic

class CoroutineLogContext(
    data: MutableLogContext = MutableLogContext(),
) : AbstractCoroutineContextElement(Key), MutableLogContext by data {

    companion object Key : CoroutineContext.Key<CoroutineLogContext> {

        @JvmStatic
        suspend fun get(): CoroutineLogContext = coroutineContext[CoroutineLogContext] ?: CoroutineLogContext(ThreadLogContext)
    }
}