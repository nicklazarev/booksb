package ru.booksb.server.loggers

import io.javalin.Context
import io.javalin.RequestLogger

class DefaultRequestLogger : RequestLogger {
    override fun handle(ctx: Context, timeMs: Float) {
        System.out.println(ctx.method() + " "  + ctx.path() + " took " + timeMs + " ms");
    }
}