package com.example.blog

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.result.view.Rendering
import reactor.core.publisher.Mono

@Controller
class HtmlController(private val repository: ArticleRepository,
					 private val properties: BlogProperties,
					 clientBuilder: WebClient.Builder) {

	private val client = clientBuilder.baseUrl("http://localhost:8080/api/comment/").build()

	@GetMapping("/")
	fun blog(model: Model) =
			repository.findAllByOrderByAddedAtDesc().flatMap { it.render() }.collectList().map {
				Rendering.view("blog").modelAttribute("title", properties.title)
						.modelAttribute("banner", properties.banner)
						.modelAttribute("articles", it)
						.build()
			}

	@GetMapping("/article/{slug}")
	fun article(@PathVariable slug: String) =
			repository.findBySlug(slug)
				.switchIfEmpty(Mono.error(IllegalArgumentException("Wrong article slug provided")))
					.flatMap { it.render() }
					.map { Rendering.view("article").modelAttribute("title", it.title).modelAttribute("article", it).build() }

	fun Article.render() =
		client.get().uri("$slug").accept(APPLICATION_JSON).exchange().flatMap {
			it.bodyToMono<List<Comment>>().map { comments -> RenderedArticle(
					slug,
					title,
					headline,
					content,
					author,
					addedAt.format(),
					comments)
			}
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
