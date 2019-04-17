package com.example.blog

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyAndAwait

class ArticleHandler(private val repository: ArticleRepository) {

	suspend fun findAll(request: ServerRequest) =
			ok().bodyAndAwait(repository.findAll())

	suspend fun findOne(request: ServerRequest) =
			ok().bodyAndAwait(repository.findBySlug(request.pathVariable("slug")) ?: throw IllegalArgumentException("Wrong article slug provided"))
}
