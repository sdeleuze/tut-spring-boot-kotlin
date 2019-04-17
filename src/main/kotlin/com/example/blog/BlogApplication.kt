package com.example.blog

import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.fu.kofu.configuration
import org.springframework.fu.kofu.r2dbc.r2dbcPostgresql
import org.springframework.fu.kofu.reactiveWebApplication
import org.springframework.fu.kofu.webflux.mustache
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

val app = reactiveWebApplication {
	configurationProperties<BlogProperties>("blog")
	enable(dataConfig)
	enable(webServerConfig)
}

fun main(args: Array<String>) {
	app.run(args)
}
