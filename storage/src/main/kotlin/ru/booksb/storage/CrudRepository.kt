package ru.booksb.storage

interface CrudRepository<ID, MODEL> {

    fun save(item: MODEL)

    fun findById(id: ID): MODEL?

    fun findAll(): List<MODEL>

    fun deleteAll()

}