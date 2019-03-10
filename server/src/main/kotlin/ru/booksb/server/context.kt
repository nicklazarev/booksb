package ru.booksb.server

import com.mongodb.client.MongoDatabase
import io.javalin.Handler
import io.javalin.security.AccessManager
import ru.booksb.config.Environment
import ru.booksb.epublib.EpubParser
import ru.booksb.epublib.EpubParserImpl
import ru.booksb.server.handlers.AllBooksHandler
import ru.booksb.server.handlers.BookContentHandler
import ru.booksb.server.handlers.CreateUserHandler
import ru.booksb.server.access.DefaultAccessManager
import ru.booksb.server.handlers.CoverHandler
import ru.booksb.server.handlers.FileSessionHandler
import ru.booksb.server.handlers.GetUsersHandler
import ru.booksb.server.handlers.LoginHandler
import ru.booksb.server.handlers.ReimportFileHandler
import ru.booksb.server.handlers.UploadFileHandler
import ru.booksb.server.loggers.DefaultRequestLogger
import ru.booksb.server.services.BooksImporterImpl
import ru.booksb.server.storage.MongoInitializer
import ru.booksb.storage.BooksRepository
import ru.booksb.storage.UsersRepository
import java.nio.file.Paths

val appctx = AppCtx()
val serverCtx = ServerCtx()
val storageCtx = StorageCtx()

class AppCtx {
    private val config = Paths.get(ClassLoader.getSystemResource("application.properties").toURI())

    val env = Environment(config)

    val database: MongoDatabase by lazy {
        MongoInitializer().connect(env.mongoConnectionString, env.mongoDatabase)
    }
}

class ServerCtx {
    val fileSessionHandler by lazy {
        FileSessionHandler(appctx.env.sessionsPath)
    }

    val requestLogger by lazy {
        DefaultRequestLogger()
    }

    val uploadFileHandler by lazy {
        UploadFileHandler(appctx.env.booksStoragePath)
    }

    val reimportFileHandler by lazy {
        ReimportFileHandler(
            storageCtx.epubParser,
            storageCtx.booksRepository,
            storageCtx.booksImporter,
            appctx.env.booksStoragePath,
            appctx.env.booksStaticPath
        )
    }

    val bookContentHandler by lazy {
        BookContentHandler(
            appctx.env.booksStoragePath,
            storageCtx.booksRepository,
            storageCtx.epubParser
        )
    }

    val allBooksHandler by lazy {
        AllBooksHandler(storageCtx.booksRepository)
    }

    val getUsersHandler by lazy {
        GetUsersHandler(storageCtx.usersRepository)
    }

    val createUserHandler by lazy {
        CreateUserHandler(storageCtx.usersRepository)
    }
    val loginHandler by lazy {
        LoginHandler(storageCtx.usersRepository)
    }
    val accessManager: AccessManager by lazy {
        DefaultAccessManager()
    }
    val coverHandler: Handler by lazy {
        CoverHandler(appctx.env.booksStaticPath)
    }

}

class StorageCtx {
    val booksRepository by lazy {
        BooksRepository(appctx.database)
    }

    val epubParser: EpubParser by lazy {
        EpubParserImpl()
    }

    val booksImporter by lazy {
        BooksImporterImpl(appctx.env.booksStaticPath, booksRepository)
    }

    val usersRepository by lazy {
        UsersRepository(appctx.database)
    }
}
