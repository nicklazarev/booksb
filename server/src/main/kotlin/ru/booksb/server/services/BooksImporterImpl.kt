package ru.booksb.server.services

import com.booksb.imageprocessing.ImageResizer
import ru.booksb.epublib.model.EpubBook
import ru.booksb.storage.BooksRepository
import ru.booksb.storage.model.Book
import ru.booksb.storage.model.Page
import java.io.ByteArrayInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID

class BooksImporterImpl(
    private val booksStaticPath: Path,
    private val booksRepository: BooksRepository
) : BooksImporter {

    override fun importBook(book: EpubBook) {
        val bookEntity = book.toBook()
        booksRepository.save(bookEntity)

        ByteArrayInputStream(book.coverImage).use { inputStream ->
            val bytes = ImageResizer.resize(inputStream, "jpeg", 200, 280)
                .toByteArray()
            val bookPath = booksStaticPath.resolve(bookEntity.id)
            Files.createDirectories(bookPath)
            Files.write(bookPath.resolve("cover.jpg"), bytes)
        }
    }

    private fun EpubBook.toBook(): Book {
        return Book(
            id = UUID.randomUUID().toString(),
            name = this.title,
            authors = authors,
            isbn = isbn,
            contents = contents.map { Page(title = it.title , link = it.link) },
            description = description,
            categories = categories,
            publisher = publisher,
            originalFileName = originalFileName
        )
    }
}