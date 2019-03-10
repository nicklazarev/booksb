package ru.booksb.server.handlers

import io.javalin.Context
import io.javalin.Handler
import java.nio.file.Files
import java.nio.file.Path

class CoverHandler(private val booksStaticPath: Path) : Handler {
    override fun handle(ctx: Context) {
        val bookId = ctx.pathParam("id")
        val coverPath = booksStaticPath.resolve(bookId).resolve("cover.jpg")
        ctx.result(Files.newInputStream(coverPath))
    }
}
