package ru.booksb.server.handlers

import io.javalin.Context
import io.javalin.Handler
import ru.booksb.epublib.EpubParser
import ru.booksb.storage.BooksRepository
import java.nio.file.Path

class BookContentHandler(
    private val booksStoragePath: Path,
    private val booksRepository: BooksRepository,
    private val epubParser: EpubParser
) : Handler {
    override fun handle(ctx: Context) {
        val bookId = ctx.pathParam("id")
        val elementId = ctx.pathParam("elementid").takeWhile { it != '#' }

        val book = booksRepository.findById(bookId)
        val content = epubParser.getFile(
            booksStoragePath.resolve(book!!.originalFileName),
            elementId
        )
        ctx.result(content)
    }
}