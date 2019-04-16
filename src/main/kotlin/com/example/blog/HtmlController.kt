package com.example.blog

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange

@Controller
class HtmlController(private val repository: ArticleRepository,
					 private val properties: BlogProperties,
					 clientBuilder: WebClient.Builder) {

	private val client = clientBuilder.baseUrl("http://localhost:8080/api/comment/").build()

	@GetMapping("/")
	suspend fun blog(model: Model): String {
		val articles = repository.findAll().map { it.render() }.toList()
		model["title"] = properties.title
		model["banner"] = properties.banner
		model["articles"] = articles
		return "blog"
	}

	@GetMapping("/article/{slug}")
	suspend fun article(@PathVariable slug: String, model: Model): String {
		val article = repository
				.findBySlug(slug)
				?.render()
				?: throw IllegalArgumentException("Wrong article slug provided")
		model["title"] = article.title
		model["article"] = article
		return "article"
	}

	suspend fun Article.render(): RenderedArticle {
		val response = client.get().uri("$slug").accept(APPLICATION_JSON).awaitExchange()
		val comments = response.awaitBody<List<Comment>>()
		return RenderedArticle(
				slug,
				title,
				headline,
				content,
				author,
				addedAt.format(),
				comments)
	}

	data class RenderedArticle(
			val slug: String,
			val title: String,
			val headline: String?,
			val content: String,
			val author: String,
			val addedAt: String,
			val comments: List<Comment>)

}
