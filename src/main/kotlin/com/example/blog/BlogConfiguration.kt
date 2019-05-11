package com.example.blog

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.DatabaseClient

@Configuration
class BlogConfiguration {

	@Bean
	fun databaseClient(): DatabaseClient {
		val configuration = PostgresqlConnectionConfiguration
				.builder()
				.host("localhost")
				.username("postgres")
				.password("")
				.build()
		return DatabaseClient.builder().connectionFactory(PostgresqlConnectionFactory(configuration)).build()
	}

	@Bean
	fun databaseInitializer(articleRepository: ArticleRepository) = ApplicationRunner {
		runBlocking {
			articleRepository.init()
		}
	}
}
