package com.example.blog

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.reactive.result.view.Rendering
import reactor.core.publisher.Mono

@Controller
class HtmlController(private val repository: ArticleRepository,
					 private val properties: BlogProperties) {

	@GetMapping("/")
	fun blog(model: Model) =
			repository.findAllByOrderByAddedAtDesc().map { it.render() }.collectList().map {
				Rendering.view("blog").modelAttribute("title", properties.title)
						.modelAttribute("banner", properties.banner)
						.modelAttribute("articles", it)
						.build()
			}

	@GetMapping("/article/{slug}")
	fun article(@PathVariable slug: String) =
			repository.findBySlug(slug)
				.switchIfEmpty(Mono.error(IllegalArgumentException("Wrong article slug provided")))
				.map { Rendering.view("article").modelAttribute("title", it.title).modelAttribute("article", it.render()).build() }

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
