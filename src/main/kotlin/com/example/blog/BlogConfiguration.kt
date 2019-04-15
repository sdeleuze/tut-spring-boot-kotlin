package com.example.blog

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BlogConfiguration {

	@Bean
	fun databaseInitializer(articleRepository: ArticleRepository) = ApplicationRunner {

		articleRepository.save(Article(
				title = "Going Reactive with Spring, Coroutines and Kotlin Flow",
				headline = "Lorem ipsum",
				content = "dolor sit amet",
				author = "Sébastien"
		))
        articleRepository.save(Article(
				title = "Spring Framework 5.2.0.M1 available now",
				headline = "Lorem ipsum",
				content = "dolor sit amet",
				author = "Brian"
		))
    }
}
