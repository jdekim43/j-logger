package kr.jadekim.logger.pipeline

import kr.jadekim.logger.Log

interface JLogPipe {

    interface Key<Pipe : JLogPipe>

    val key: Key<out JLogPipe>

    fun install(pipeline: MutableList<JLogPipe>, index: Int) {
        pipeline.add(index, this)
    }

    fun handle(log: Log): Log?
}

fun List<JLogPipe>.handle(log: Log) {
    var temp: Log? = log

    for (each in this) {
        if (temp == null) {
            break
        }

        temp = each.handle(temp)
    }
}

fun List<JLogPipe>.contains(key: JLogPipe.Key<out JLogPipe>): Boolean = find { it.key == key } != null