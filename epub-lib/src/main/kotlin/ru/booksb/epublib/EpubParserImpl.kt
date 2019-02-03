package ru.booksb.epublib

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import ru.booksb.epublib.model.EpubBook
import ru.booksb.epublib.model.Page
import java.io.InputStream
import java.nio.file.Path
import java.util.zip.ZipFile
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class EpubParserImpl : EpubParser {
    private val factory = DocumentBuilderFactory.newInstance()
    private val builder = factory.newDocumentBuilder()
    val xPathfactory = XPathFactory.newInstance()
    val xpath = xPathfactory.newXPath()

    private val metadataFileName = "META-INF/container.xml"

    override fun parse(path: Path): EpubBook {
        val epubFile = ZipFile(path.toFile())
        val metadata = epubFile.getEntryDocument(metadataFileName)
        val contentFileLocation = select(metadata, "//*[name()='rootfile']/@full-path")
        val content = epubFile.getEntryDocument(contentFileLocation)

        return EpubBook(
            isbn = select(content, "//*[name()='dc:identifier']"),
            title = select(content, "//*[name()='dc:title']"),
            authors = selectAll(content, "//*[name()='dc:creator']"),
            description = select(content, "//*[name()='dc:description']"),
            categories = selectAll(content, "//*[name()='dc:subject']"),
            publisher = select(content, "//*[name()='dc:publisher']"),
            coverImage = select(content, "//*[name()='meta'][@name='cover']/@content")
                .let { select(content, "//*[name()='item'][@id='$it']/@href") }
                .let { epubFile.getInputStream(epubFile.getEntry(it)).readBytes() },
            contents = selectNode(content, "//*[name()='spine']")
                .let { spine ->
                    val tocNcxId = spine.attributes.getNamedItem("toc").nodeValue
                    if (tocNcxId == null) {
                        emptyList()
                    } else {
                        val tocFileLocation = select(content, "//*[name()='item'][@id='$tocNcxId']/@href")
                        val toc = epubFile.getEntryDocument(tocFileLocation)
                        val nodes = selectNodes(toc, "//*[name()='navPoint']")
                        (0 until nodes.length)
                            .map {
                                val node = nodes.item(it)
                                Page(
                                    title = select(node, "*[name()='navLabel']/*[name()='text']"),
                                    link = select(node, "*[name()='content']/@src")
                                )
                            }
                    }
                },
            originalFileName = path.fileName.toString()
        )
    }

    override fun getFile(bookPath: Path, filePath: String): InputStream {
        val epubFile = ZipFile(bookPath.toFile())
        return epubFile.getEntryContent(filePath)
    }

    private fun ZipFile.getEntryContent(entryName: String): InputStream {
        val entry = getEntry(entryName)
        return getInputStream(entry)
    }

    private fun ZipFile.getEntryDocument(entryName: String): Document {
        return getEntryContent(entryName).use { builder.parse(it) }
    }

    private fun select(doc: Any, xpathExpression: String): String {
        val expr = xpath.compile(xpathExpression)
        return expr.evaluate(doc, XPathConstants.STRING) as String
    }

    private fun selectNode(doc: Document, xpathExpression: String): Node {
        val expr = xpath.compile(xpathExpression)
        return expr.evaluate(doc, XPathConstants.NODE) as Node
    }

    private fun selectNodes(doc: Document, xpathExpression: String): NodeList {
        val expr = xpath.compile(xpathExpression)
        return expr.evaluate(doc, XPathConstants.NODESET) as NodeList
    }

    private fun selectAll(doc: Document, xpathExpression: String): List<String> {
        val expr = xpath.compile(xpathExpression)
        val nodeList = (expr.evaluate(doc, XPathConstants.NODESET) as NodeList)
        return (0..nodeList.length).mapNotNull { nodeList.item(it)?.textContent }
    }
}