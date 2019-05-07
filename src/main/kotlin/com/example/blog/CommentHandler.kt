package com.example.blog

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyFlowAndAwait

class CommentHandler(private val repository: ArticleRepository) {

	suspend fun findAllForArticle(request: ServerRequest) = ok().bodyFlowAndAwait(flow {
		delay(100)
		emit(Comment("This is a comment from a slow remote HTTP endpoint", "Sébastien Deleuze"))
		delay(100)
		emit(Comment("This is another comment from a slow remote HTTP endpoint", "Sébastien Deleuze"))
	})
}
