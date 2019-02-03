package ru.booksb.server.handlers

import io.javalin.Context
import io.javalin.Handler
import ru.booksb.storage.UsersRepository

class GetUsersHandler(private val usersRepository: UsersRepository) : Handler {
    override fun handle(ctx: Context) {
        ctx.json(usersRepository.findAll())
    }
}
