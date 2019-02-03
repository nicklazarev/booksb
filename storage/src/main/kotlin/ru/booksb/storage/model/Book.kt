package ru.booksb.storage.model

import org.bson.codecs.pojo.annotations.BsonId

data class Book(
    @BsonId
    val id: String,
    val name: String,
    val authors: List<String>,
    val isbn: String,
    val contents: List<Page>,
    val description: String,
    val categories: List<String>,
    val publisher: String,
    val originalFileName: String
)