package com.example.blog

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.renderAndAwait

class HtmlHandler(private val repository: ArticleRepository,
				  private val properties: BlogProperties) {

	suspend fun blog(request: ServerRequest): ServerResponse {
		val articles = repository.findAll().map { it.render() }.toList()
		val model = mapOf(
				"title" to properties.title,
				"banner" to properties.banner,
				"articles" to articles)
		return ok().renderAndAwait("blog", model)
	}

	suspend fun article(request: ServerRequest): ServerResponse {
		val article = repository
				.findBySlug(request.pathVariable("slug"))
				?.render()
				?: throw IllegalArgumentException("Wrong article slug provided")
		val model = mapOf(
				"title" to article.title,
				"article" to article)
		return ok().renderAndAwait("article", model)
	}

	fun Article.render() = RenderedArticle(
			slug,
			title,
			headline,
			content,
			author,
			addedAt.format()
	)

	data class RenderedArticle(
			val slug: String,
			val title: String,
			val headline: String?,
			val content: String,
			val author: String,
			val addedAt: String)

}
