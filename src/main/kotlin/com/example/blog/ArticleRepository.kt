package com.example.blog

import org.springframework.data.r2dbc.function.DatabaseClient
import org.springframework.data.r2dbc.function.asType
import org.springframework.data.r2dbc.function.await
import org.springframework.data.r2dbc.function.awaitOne
import org.springframework.data.r2dbc.function.awaitOneOrNull
import org.springframework.data.r2dbc.function.flow
import org.springframework.data.r2dbc.function.into
import org.springframework.stereotype.Repository

@Repository
class ArticleRepository(private val client: DatabaseClient) {

	suspend fun findBySlug(slug: String) =
		client.execute()
				.sql("SELECT * FROM articles WHERE slug = \$1")
				.bind(0, slug).asType<Article>()
				.fetch().awaitOneOrNull()

	fun findAll() =
		client.execute()
				.sql("SELECT * FROM articles ORDER BY added_at DESC")
				.asType<Article>()
				.fetch().flow()

	suspend fun save(article: Article) =
			client.insert().into<Article>().table("articles").using(article).await()

	suspend fun deleteAll() =
			client.execute().sql("DELETE FROM articles").await()

	suspend fun init() {
		client.execute().sql("CREATE TABLE IF NOT EXISTS articles (slug VARCHAR PRIMARY KEY, title VARCHAR, headline VARCHAR, content VARCHAR, author VARCHAR, added_at TIMESTAMP);").await()
		deleteAll()
		save(Article(
			title = "Going Reactive with Spring, Coroutines and Kotlin Flow",
			headline = "Lorem ipsum",
			content = "dolor sit amet",
			author = "Sébastien"))
		save(Article(
				title = "Spring Framework 5.2.0.M1 available now",
				headline = "Lorem ipsum",
				content = "dolor sit amet",
				author = "Brian"))
	}

}