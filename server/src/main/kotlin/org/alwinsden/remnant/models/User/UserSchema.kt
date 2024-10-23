package org.alwinsden.remnant.models.User

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ExposedUser(val name: String, val age: Int)
class UserSchemaService(private val database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val age = integer("age")

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database){
            SchemaUtils.create(Users)
        }
    }
}