package com.example.blog

data class BlogProperties(val title: String, val banner: Banner)

data class Banner(val title: String?, val content: String)

