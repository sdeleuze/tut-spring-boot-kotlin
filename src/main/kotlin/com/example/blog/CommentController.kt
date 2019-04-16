package com.example.blog

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/comment")
class CommentController(private val repository: ArticleRepository) {

	@GetMapping("/{slug}")
	fun findAllForArticle() = flow {
		delay(100)
		emit(Comment("This is a comment from a slow remote HTTP endpoint", "Sébastien Deleuze"))
		delay(100)
		emit(Comment("This is another comment from a slow remote HTTP endpoint", "Sébastien Deleuze"))
	}
}