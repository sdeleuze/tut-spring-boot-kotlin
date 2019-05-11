package com.example.blog

import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.asType
import org.springframework.data.r2dbc.core.into
import org.springframework.stereotype.Repository

@Repository
class ArticleRepository(private val client: DatabaseClient) {

	fun findBySlug(slug: String) =
		client.execute()
				.sql("SELECT * FROM Articles WHERE slug = :slug")
				.bind("slug", slug).asType<Article>()
				.fetch().one()

	fun findAllByOrderByAddedAtDesc() =
		client.execute()
				.sql("SELECT * FROM Articles ORDER BY added_at DESC")
				.asType<Article>()
				.fetch().all()

	fun save(article: Article) =
			client.insert().into<Article>().table("Articles").using(article).then()

	fun deleteAll() =
			client.execute().sql("DELETE FROM Articles").then()

	fun init() = client.execute().sql("CREATE TABLE IF NOT EXISTS Articles (slug VARCHAR PRIMARY KEY, title VARCHAR, headline VARCHAR, content VARCHAR, author VARCHAR, added_at TIMESTAMP);").then()
			.then(deleteAll())
			.then(save(Article(
					title = "Going Reactive with Spring, Coroutines and Kotlin Flow",
					headline = "Lorem ipsum",
					content = "dolor sit amet",
					author = "Sébastien")))
			.then(save(Article(
					title = "Spring Framework 5.2.0.M1 available now",
					headline = "Lorem ipsum",
					content = "dolor sit amet",
					author = "Brian"
			)))

}