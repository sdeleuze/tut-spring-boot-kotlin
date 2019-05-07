package com.example.blog

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux.concat
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.cast
import java.time.Duration

@RestController
@RequestMapping("/api/comment")
class CommentController(private val repository: ArticleRepository) {

	@GetMapping("/{slug}")
	fun findAllForArticle() = concat(
			Mono.delay(Duration.ofMillis(100)).then().cast<Comment>(),
			Mono.just(Comment("This is a comment from a slow remote HTTP endpoint", "Sébastien Deleuze")),
			Mono.delay(Duration.ofMillis(100)).then().cast<Comment>(),
			Mono.just(Comment("This is another comment from a slow remote HTTP endpoint", "Sébastien Deleuze"))
	)
}