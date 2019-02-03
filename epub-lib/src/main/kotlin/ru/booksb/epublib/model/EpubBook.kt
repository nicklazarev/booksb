package ru.booksb.epublib.model

data class EpubBook(
    val title: String,
    val authors: List<String>,
    val isbn: String,
    val contents: List<Page>,
    val description: String,
    val categories: List<String>,
    val publisher: String,
    val coverImage: ByteArray,
    val originalFileName: String
)

data class Page(
    val title: String,
    val link: String
)