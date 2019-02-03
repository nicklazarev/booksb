package ru.booksb.epublib

import ru.booksb.epublib.model.EpubBook
import java.io.InputStream
import java.nio.file.Path

interface EpubParser {

    fun parse(path: Path): EpubBook

    fun getFile(bookPath: Path, filePath: String): InputStream

}