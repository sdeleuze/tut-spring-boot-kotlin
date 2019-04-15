package com.example.blog

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/article")
class ArticleController(private val repository: ArticleRepository) {

	@GetMapping("/")
	fun findAll() = repository.findAll()

	@GetMapping("/{slug}")
	suspend fun findOne(@PathVariable slug: String) =
			repository.findBySlug(slug) ?: throw IllegalArgumentException("Wrong article slug provided")

}