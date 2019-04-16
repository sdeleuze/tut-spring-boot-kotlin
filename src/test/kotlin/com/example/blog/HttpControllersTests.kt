package com.example.blog

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList

@WebFluxTest
@AutoConfigureWebClient
class HttpControllersTests(@Autowired val client: WebTestClient) {

	@MockkBean
	lateinit var articleRepository: ArticleRepository

	@Test
	fun `List articles`() {
		val spring5Article = Article("Spring Framework 5.0 goes GA", "Dear Spring community ...", "Juergen Hoeller")
		val spring43Article = Article("Spring Framework 4.3 goes GA", "Dear Spring community ...", "Juergen Hoeller")
		every { articleRepository.findAll() } returns flow {
			emit(spring5Article)
			emit(spring43Article)
		}
		client.get().uri("/api/article/").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBodyList<Article>()
				.hasSize(2)
				.contains(spring5Article)
				.contains(spring43Article)
	}
}
