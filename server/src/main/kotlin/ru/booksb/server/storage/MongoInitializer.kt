package ru.booksb.server.storage

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo

class MongoInitializer {
    fun connect(connection: String, databaseName: String): MongoDatabase {
        val client = KMongo.createClient(connection)
        return client.getDatabase(databaseName)
    }
}