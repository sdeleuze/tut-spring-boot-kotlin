package com.example.blog

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyAndAwait

class CommentHandler(private val repository: ArticleRepository) {

	suspend fun findAllForArticle(request: ServerRequest) = ok().bodyAndAwait(flow {
		delay(100)
		emit(Comment("This is a comment from a slow remote HTTP endpoint", "Sébastien Deleuze"))
		delay(100)
		emit(Comment("This is another comment from a slow remote HTTP endpoint", "Sébastien Deleuze"))
	})
}
