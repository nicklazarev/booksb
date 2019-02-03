package ru.booksb.storage

import com.mongodb.client.MongoDatabase
import ru.booksb.storage.model.Book

class BooksRepository (db: MongoDatabase):
    CrudRepositoryImpl<String, Book>(db, "books", Book::class.java)