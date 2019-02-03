package ru.booksb.server.services

import ru.booksb.epublib.model.EpubBook

interface BooksImporter {

    fun importBook(book: EpubBook)

}