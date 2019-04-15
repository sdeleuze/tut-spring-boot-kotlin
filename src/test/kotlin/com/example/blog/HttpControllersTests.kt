package com.example.blog

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
class HttpControllersTests(@Autowired val mockMvc: MockMvc) {

	@MockkBean
	lateinit var articleRepository: ArticleRepository

	@Test
	fun `List articles`() {
		val author = "Juergen Hoeller"
		val spring5Article = Article("Spring Framework 5.0 goes GA", "Dear Spring community ...", "Lorem ipsum", author)
		val spring43Article = Article("Spring Framework 4.3 goes GA", "Dear Spring community ...", "Lorem ipsum", author)
		every { articleRepository.findAllByOrderByAddedAtDesc() } returns listOf(spring5Article, spring43Article)
		mockMvc.get("/api/article/") {
			accept(MediaType.APPLICATION_JSON)
		}.andExpect {
			status().isOk
			content().contentType(MediaType.APPLICATION_JSON_UTF8)
			jsonPath("\$.[0].author").value(author)
			jsonPath("\$.[0].slug").value(spring5Article.slug)
			jsonPath("\$.[1].author").value(author)
			jsonPath("\$.[1].slug").value(spring43Article.slug)
		}
	}
}
