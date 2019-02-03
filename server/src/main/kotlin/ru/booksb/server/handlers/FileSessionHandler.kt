package ru.booksb.server.handlers

import org.eclipse.jetty.server.session.DefaultSessionCache
import org.eclipse.jetty.server.session.FileSessionDataStore
import org.eclipse.jetty.server.session.SessionHandler
import java.nio.file.Path

class FileSessionHandler(private val sessionsPath: Path) : SessionHandler() {
    init {
        httpOnly = true
        sessionCache = DefaultSessionCache(this).apply {
            sessionDataStore = FileSessionDataStore().apply {
                storeDir = sessionsPath.toFile().apply { mkdir() }
            }
        }
    }
}