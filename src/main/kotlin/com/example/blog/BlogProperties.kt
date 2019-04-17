package com.example.blog

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("blog")
data class BlogProperties(val title: String, val banner: Banner) {
	data class Banner(val title: String?, val content: String)
}
