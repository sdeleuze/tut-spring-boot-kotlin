package com.example.blog

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class Article(
		val title: String,
		val content: String,
		val author: String,
		val headline: String? = null,
		@Id val slug: String = title.toSlug(),
		var addedAt: LocalDateTime = LocalDateTime.now())
