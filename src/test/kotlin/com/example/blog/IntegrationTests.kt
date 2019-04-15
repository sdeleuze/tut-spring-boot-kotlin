package com.example.blog

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.*
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class IntegrationTests(@Autowired val restTemplate: TestRestTemplate) {

	@Test
	fun `Assert blog page title, content and status code`() {
		val entity = restTemplate.getForEntity<String>("/")
		assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
		assertThat(entity.body).contains("<h1>Blog</h1>", "Coroutines")
	}

	@Test
	fun `Assert article page title, content and status code`() {
		val title = "Going Reactive with Spring, Coroutines and Kotlin Flow"
		val entity = restTemplate.getForEntity<String>("/article/${title.toSlug()}")
		assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
		assertThat(entity.body).contains(title, "Lorem ipsum", "dolor sit amet")
	}
}
