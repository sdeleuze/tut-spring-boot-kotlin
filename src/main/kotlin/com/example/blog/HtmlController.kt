package com.example.blog

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class HtmlController(private val repository: ArticleRepository,
					 private val properties: BlogProperties) {

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
