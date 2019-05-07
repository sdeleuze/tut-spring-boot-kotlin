package com.example.blog

import kotlinx.coroutines.runBlocking
import org.springframework.boot.WebApplicationType
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.configuration
import org.springframework.fu.kofu.r2dbc.r2dbcPostgresql
import org.springframework.fu.kofu.webflux.mustache
import org.springframework.fu.kofu.webflux.webClient
import org.springframework.fu.kofu.webflux.webFlux

val dataConfig = configuration {
	beans {
		bean<ArticleRepository>()
	}
	r2dbcPostgresql()
	listener<ApplicationReadyEvent> {
		runBlocking {
			ref<ArticleRepository>().init()
		}
	}
}

val webServerConfig = configuration {
	beans {
		bean<ArticleHandler>()
		bean<HtmlHandler>()
		bean<CommentHandler>()
		bean(::blogRouter)
	}
	webFlux {
		codecs {
			string()
			jackson()
		}
		mustache()
	}
}

val webClientConfig = configuration {
	webClient {
		codecs {
			string()
			jackson()
		}
	}
}

val app = application(WebApplicationType.REACTIVE) {
	configurationProperties<BlogProperties>("blog")
	enable(dataConfig)
	enable(webServerConfig)
	enable(webClientConfig)
}

fun main(args: Array<String>) {
	app.run(args)
}
