package ru.booksb.server.handlers

import io.javalin.Context
import io.javalin.Handler
import ru.booksb.storage.BooksRepository

class AllBooksHandler(
    private val booksRepository: BooksRepository
): Handler {
    override fun handle(ctx: Context) {
        val name = ctx.queryParam("name")
        val result = if (name.isNullOrEmpty()) {
            booksRepository.findAll()
        } else {
            booksRepository.findByName(name)
        }
        ctx.json(result)
    }
}
