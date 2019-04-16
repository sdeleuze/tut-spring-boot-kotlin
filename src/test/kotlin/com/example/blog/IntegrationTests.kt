package com.example.blog

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.*
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class IntegrationTests(@Autowired val client: WebTestClient) {

	@Test
	fun `Assert blog page title, content and status code`() {
		client.get().uri("/").exchange().expectStatus().isOk.expectBody<String>().consumeWith {
			assertThat(it.responseBody).contains("<h1>Blog</h1>", "Coroutines")
		}
	}

	@Test
	fun `Assert article page title, content and status code`() {
		val title = "Going Reactive with Spring, Coroutines and Kotlin Flow"
		client.get().uri("/article/${title.toSlug()}").exchange().expectStatus().isOk.expectBody<String>().consumeWith {
			assertThat(it.responseBody).contains(title, "Lorem ipsum", "dolor sit amet")
		}
	}

}