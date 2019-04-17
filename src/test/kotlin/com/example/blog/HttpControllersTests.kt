package com.example.blog

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.fu.kofu.reactiveWebApplication
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList

class HttpControllersTests {

	private val client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build()
	private val spring5Article = Article("Spring Framework 5.0 goes GA", "Dear Spring community ...", "Juergen Hoeller")
	private val spring43Article = Article("Spring Framework 4.3 goes GA", "Dear Spring community ...", "Juergen Hoeller")
	private lateinit var context: ConfigurableApplicationContext

	@BeforeAll
	fun setup() {
		val app = reactiveWebApplication {
			configurationProperties<BlogProperties>("blog")
			enable(webServerConfig)
			beans {
				bean {
					val articleRepository = mockk<ArticleRepository>()
					every { articleRepository.findAll() } returns flow {
						emit(spring5Article)
						emit(spring43Article)
					}
					articleRepository
				}
			}
		}
		context = app.run()
	}

	@Test
	fun `List articles`() {
		client.get().uri("/api/article/").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBodyList<Article>()
				.hasSize(2)
				.contains(spring5Article)
				.contains(spring43Article)
	}

	@AfterAll
	fun teardown() {
		context.close()
	}
}
