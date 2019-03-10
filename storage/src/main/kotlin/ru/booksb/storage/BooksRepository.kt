package ru.booksb.storage

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.find
import org.litote.kmongo.regex
import ru.booksb.storage.model.Book

class BooksRepository (db: MongoDatabase):
    CrudRepositoryImpl<String, Book>(db, "books", Book::class.java) {

    fun findByName(name: String): List<Book> {
        return collection.find(Book::name regex ".*$name.*".toRegex(RegexOption.IGNORE_CASE)).toList()
    }
}