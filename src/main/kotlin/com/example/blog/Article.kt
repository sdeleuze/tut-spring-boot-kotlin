package com.example.blog

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Article(
		var title: String,
		var headline: String,
		var content: String,
		var author: String,
		var slug: String = title.toSlug(),
		var addedAt: LocalDateTime = LocalDateTime.now(),
		@Id @GeneratedValue var id: Long? = null)
