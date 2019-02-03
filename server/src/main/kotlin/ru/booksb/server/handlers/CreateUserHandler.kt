package ru.booksb.server.handlers

import io.javalin.Context
import io.javalin.Handler
import ru.booksb.storage.UsersRepository
import ru.booksb.storage.model.User

class CreateUserHandler(private val usersRepository: UsersRepository) : Handler {
    override fun handle(ctx: Context) {
        val user = ctx.bodyAsClass(User::class.java)
        usersRepository.save(user)
    }
}
