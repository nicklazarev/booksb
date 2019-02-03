package ru.booksb.server.access

import io.javalin.Context
import io.javalin.Handler
import io.javalin.security.AccessManager
import io.javalin.security.Role
import ru.booksb.server.Roles

class DefaultAccessManager : AccessManager {
    override fun manage(handler: Handler, ctx: Context, permittedRoles: MutableSet<Role>) {
        val userRoles = mutableListOf(Roles.ANY)
        val isUserLoggedIn = ctx.sessionAttribute<String>("user") != null
        if (isUserLoggedIn) {
            userRoles.add(Roles.LOGGED_IN)
        }

        if (permittedRoles.intersect(userRoles).isNotEmpty()) {
            handler.handle(ctx)
        } else {
            ctx.status(401).result("Unauthorized")
        }
    }
}
