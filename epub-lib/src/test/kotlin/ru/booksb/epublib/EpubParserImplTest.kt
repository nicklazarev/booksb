package ru.booksb.epublib

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.nio.file.Paths

class EpubParserImplTest {

    @Test
    fun parse() {
        val parser = EpubParserImpl()
        val testbookPath = Paths.get(ClassLoader.getSystemResource("testbook.epub").toURI())
        val epubBook = parser.parse(testbookPath)

        assertThat(epubBook.title).isEqualTo("Программист-фанатик")
        println(epubBook)
    }
}