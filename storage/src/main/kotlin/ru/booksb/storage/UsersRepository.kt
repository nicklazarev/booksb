package ru.booksb.storage

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.eq
import ru.booksb.storage.model.User

class UsersRepository(database: MongoDatabase)
    : CrudRepositoryImpl<String, User>(database, "users", User::class.java) {

    fun findByUserName(username: String): User? {
        return collection.find(User::username eq username).limit(1).first()
    }
}
