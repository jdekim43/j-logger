package kr.jadekim.logger.integration.fuel

import com.github.kittinunf.fuel.core.FoldableRequestInterceptor
import com.github.kittinunf.fuel.core.FoldableResponseInterceptor
import com.github.kittinunf.fuel.core.RequestTransformer
import com.github.kittinunf.fuel.core.ResponseTransformer


@Suppress("FunctionName")
fun FoldableRequestInterceptor(block: RequestTransformer) = object : FoldableRequestInterceptor {

    override fun invoke(next: RequestTransformer): RequestTransformer = {
        next(block(it))
    }
}

@Suppress("FunctionName")
fun FoldableResponseInterceptor(block: ResponseTransformer) = object : FoldableResponseInterceptor {

    override fun invoke(next: ResponseTransformer): ResponseTransformer = { request, response ->
        next(request, block(request, response))
    }
}