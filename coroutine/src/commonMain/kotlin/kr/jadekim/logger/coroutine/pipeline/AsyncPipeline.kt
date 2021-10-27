package kr.jadekim.logger.coroutine.pipeline

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.launch
import kr.jadekim.logger.Log
import kr.jadekim.logger.pipeline.JLogPipe
import kr.jadekim.logger.pipeline.handle
import kotlin.coroutines.CoroutineContext

class AsyncPipeline(
    val pipeline: List<JLogPipe>,
    queueCapacity: Int = Channel.UNLIMITED,
) : CoroutineScope, JLogPipe {

    companion object Key : JLogPipe.Key<AsyncPipeline>

    override val coroutineContext: CoroutineContext = CoroutineName("JLogger-AsyncPipeline")

    private val queue = Channel<Log>(queueCapacity)

    init {
        launch {
            for (log in queue) {
                pipeline.handle(log)
            }
        }
    }

    override val key: JLogPipe.Key<out JLogPipe> = Key

    override fun handle(log: Log): Log {
        // 최대한 순서를 보장해주기위해 trySend 를 먼저함
        // TODO: 순서를 보장할 수 있는 다른 좋은 방법 고민 필요
        queue.trySend(log).onFailure {
            launch { queue.send(log) }
        }

        return log
    }
}