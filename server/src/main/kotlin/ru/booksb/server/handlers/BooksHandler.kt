package ru.booksb.server.handlers

import io.javalin.Context
import io.javalin.Handler
import ru.booksb.storage.BooksRepository

class BooksHandler(
    private val booksRepository: BooksRepository
): Handler {
    override fun handle(ctx: Context) {
        val id = ctx.pathParam("id")
        val result = booksRepository.findById(id)
        if (result == null) {
            ctx.status(404)
        } else {
            ctx.json(result)
        }
    }
}
