package ru.booksb.server

import io.javalin.Javalin
import io.javalin.security.Role
import io.javalin.security.SecurityUtil.roles
import org.slf4j.LoggerFactory
import ru.booksb.server.Roles.*
import java.nio.file.Files
import java.nio.file.Path

private val logger = LoggerFactory.getLogger("ru.books.server.Initializer")


enum class Roles : Role {
    LOGGED_IN, ANY, ADMIN
}

fun main() {
    init()

    Javalin.create()
        .sessionHandler { serverCtx.fileSessionHandler }
        .requestLogger(serverCtx.requestLogger)
        .accessManager(serverCtx.accessManager)
        .post("/api/v1/login", serverCtx.loginHandler, roles(ANY))
        .get("/api/v1/users", serverCtx.getUsersHandler, roles(ADMIN))
        .post("/api/v1/users", serverCtx.createUserHandler, roles(ADMIN))
        .post("/api/v1/books/upload", serverCtx.uploadFileHandler, roles(ADMIN))
        .post("/api/v1/books/reimport", serverCtx.reimportFileHandler, roles(ADMIN))
        .get("/api/v1/books", serverCtx.allBooksHandler, roles(LOGGED_IN))
        .get("/api/v1/books/:id/view", serverCtx.bookContentHandler, roles(LOGGED_IN))
        .get("/api/v1/books/:id/view/:elementid", serverCtx.bookContentHandler, roles(LOGGED_IN))
        .get("/api/v1/books/:id/cover", serverCtx.coverHandler, roles(ANY))
        .get("/api/v1/books/:id/marks") { TODO() }
        .post("/api/v1/books/:id/marks") { TODO() }
        .get("/api/v1/books/:id/notes") { TODO() }
        .post("/api/v1/books/:id/notes") { TODO() }
        .get("/api/v1/books/marks") { TODO() }
        .get("/api/v1/books/notes") { TODO() }
        .start(appctx.env.port)
}

private fun init() {
    listOf(appctx.env.booksStoragePath, appctx.env.booksStaticPath)
        .forEach(::createFolderIfNotExists)
}

private fun createFolderIfNotExists(path: Path) {
    if (!Files.exists(path)) {
        logger.warn("Path: ${path.toAbsolutePath()} not exists")
        Files.createDirectories(path)
    }
}

