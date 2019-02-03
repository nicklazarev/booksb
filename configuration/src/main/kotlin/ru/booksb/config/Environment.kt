package ru.booksb.config

import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Properties

class Environment(private val configurationPath: Path) {

    private val props = Properties().apply {
        Files.newInputStream(configurationPath).use { load(it) }
    }

    val port = getPropertySafely("server.port").toInt()
    val sessionsPath = Paths.get(getPropertySafely("server.sessions.store"))
    val booksStoragePath = Paths.get(getPropertySafely("server.books.store"))
    val booksStaticPath = Paths.get(getPropertySafely("server.books.static"))
    val mongoConnectionString = getPropertySafely("server.mongo.connection")
    val mongoDatabase = getPropertySafely("server.mongo.database")

    private fun getPropertySafely(propertyName: String): String {
        return props.getProperty(propertyName)
            ?: throw RuntimeException("Property $propertyName not found in configuration")
    }
}