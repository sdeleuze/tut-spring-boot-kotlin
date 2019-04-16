package com.example.blog

import org.springframework.web.reactive.function.server.coRouter

fun blogRouter(articleHandler : ArticleHandler,
			   htmlHandler: HtmlHandler,
			   commentHandler: CommentHandler) = coRouter {
	GET("/", htmlHandler::blog)
	GET("/article/{slug}", htmlHandler::article)
	"/api/article".nest {
		GET("/", articleHandler::findAll)
		GET("/{slug}", articleHandler::findOne)
	}
	GET("/api/comment/{slug}", commentHandler::findAllForArticle)
}