package ru.booksb.server.handlers

import io.javalin.Context
import io.javalin.Handler
import ru.booksb.epublib.EpubParser
import ru.booksb.server.services.BooksImporter
import ru.booksb.storage.BooksRepository
import java.nio.file.Files
import java.nio.file.Path

class ReimportFileHandler(
    private val epubParser: EpubParser,
    private val booksRepository: BooksRepository,
    private val booksImporter: BooksImporter,
    private val booksStoragePath: Path,
    private val booksStaticPath: Path
    ) : Handler {
    override fun handle(ctx: Context) {
        booksRepository.deleteAll()
        Files.walk(booksStaticPath)
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach { it.delete() }

        Files.list(booksStoragePath)
            .map(epubParser::parse)
            .forEach(booksImporter::importBook)
    }
}