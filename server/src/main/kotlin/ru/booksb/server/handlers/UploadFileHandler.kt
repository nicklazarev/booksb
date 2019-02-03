package ru.booksb.server.handlers

import io.javalin.Context
import io.javalin.Handler
import java.nio.file.Files
import java.nio.file.Path

class UploadFileHandler(
    private val booksStoragePath: Path
) : Handler {
    override fun handle(ctx: Context) {
        ctx.uploadedFiles("file").forEach { file ->
            file.content.use { ins ->
                Files.newOutputStream(booksStoragePath.resolve(file.name)).use { out ->
                    ins.copyTo(out)
                }
            }
        }
        ctx.result("OK")
    }
}