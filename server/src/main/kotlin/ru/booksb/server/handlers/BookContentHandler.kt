package ru.booksb.server.handlers

import io.javalin.Context
import io.javalin.Handler
import ru.booksb.epublib.EpubParser
import ru.booksb.storage.BooksRepository
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.file.Path

class BookContentHandler(
    private val booksStoragePath: Path,
    private val booksRepository: BooksRepository,
    private val epubParser: EpubParser
) : Handler {
    override fun handle(ctx: Context) {
        val bookId = ctx.pathParam("id")
        val elementId = ctx.pathParamMap()["elementid"].orEmpty()
        val book = booksRepository.findById(bookId)

        val pageFileName = if (elementId.isEmpty()) {
            book!!.contents.first().link
        } else {
            elementId
        }.takeWhile { it != '#' }

        val content = epubParser.getFile(
            booksStoragePath.resolve(book!!.originalFileName),
            pageFileName
        )

        val contentTypeHandler = defineContentType(bookId, elementId)
        val modifiedContent = contentTypeHandler.handle(content)
        ctx.contentType("text/plain;charset=utf-8")
        ctx.result(ByteArrayInputStream(modifiedContent.toByteArray()))
    }

    private fun defineContentType(bookId: String, pageName: String): ContentHandler {
        return when {
            pageName.endsWith("html") || pageName.isEmpty() -> HtmlContentHandler(bookId)
            pageName.endsWith("css") -> CssContentHandler()
            else -> PassByContentHandler()
        }
    }
}

interface ContentHandler {
    fun handle(istream: InputStream): ByteArrayOutputStream
}

open class PassByContentHandler : ContentHandler {
    override fun handle(istream: InputStream): ByteArrayOutputStream {
        val byteArrayOutputStream = ByteArrayOutputStream()
        istream.copyTo(byteArrayOutputStream)
        return byteArrayOutputStream
    }
}

class HtmlContentHandler(private val bookId: String) : ContentHandler, PassByContentHandler() {
    override fun handle(istream: InputStream): ByteArrayOutputStream {
        val content = String(istream.readBytes())
        val byteArrayOutputStream = ByteArrayOutputStream()
        val rewriteLinks: String = content
            .replace("href=\"([a-zA-Z#0-9._]*)\"".toRegex()) { matchResult: MatchResult ->
                val href = matchResult.groupValues[1]
                "href=/api/v1/books/$bookId/view/$href"
            }
            .replace("src=\"([a-zA-Z#0-9._]*)\"".toRegex()) { matchResult: MatchResult ->
                val href = matchResult.groupValues[1]
                "src=/api/v1/books/$bookId/view/$href"
            }

        byteArrayOutputStream.write(rewriteLinks.toByteArray())
        return byteArrayOutputStream
    }
}

class CssContentHandler : ContentHandler {
    override fun handle(istream: InputStream): ByteArrayOutputStream {
        val content = String(istream.readBytes())
        val byteArrayOutputStream = ByteArrayOutputStream()
        val removedBodyTag = content.replace("body[,]*".toRegex(), "")
        val result = removedBodyTag.replace("(.*)\\{".toRegex()) { match: MatchResult ->
            match.groupValues.first().split(",").joinToString(",") { ".page ${it.trim()}" }
        }
        byteArrayOutputStream.write(result.toByteArray())
        return byteArrayOutputStream
    }
}