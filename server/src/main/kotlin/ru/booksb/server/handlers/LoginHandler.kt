package ru.booksb.server.handlers

import io.javalin.Context
import io.javalin.Handler
import io.javalin.UnauthorizedResponse
import ru.booksb.storage.UsersRepository

class LoginHandler(private val usersRepository: UsersRepository) : Handler {
    override fun handle(ctx: Context) {
        if (ctx.sessionAttribute<String>("user") == null) {
            val loginRequest = ctx.bodyAsClass(LoginRequest::class.java)
            val user = usersRepository.findByUserName(loginRequest.username)
            if (user != null) {
                ctx.sessionAttribute("user", loginRequest.username)
                ctx.result("OK")
                return
            }
        }

        throw UnauthorizedResponse()
    }

    data class LoginRequest(
        val username: String
    )
}
