package ru.booksb.storage

import com.mongodb.BasicDBObject
import com.mongodb.client.MongoDatabase

open class CrudRepositoryImpl<ID, MODEL>(
    db: MongoDatabase,
    collectionName: String,
    clazz: Class<MODEL>
) : CrudRepository<ID, MODEL> {
    protected val collection = db.getCollection(collectionName, clazz)

    override fun save(item: MODEL) {
        collection.insertOne(item)
    }

    override fun findById(id: ID): MODEL? {
        return collection.find(BasicDBObject("_id", id)).limit(1).singleOrNull()
    }

    override fun findAll(): List<MODEL> {
        return collection.find().toList()
    }

    override fun deleteAll() {
        collection.deleteMany(BasicDBObject(emptyMap<Any, Any>()))
    }
}