package org.alwinsden.remnant.models.User

import kotlinx.coroutines.Dispatchers
import org.alwinsden.remnant.api_data_class.ExposedUser
import org.alwinsden.remnant.api_data_class.ExposedUserWithId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class UserSchemaService(private val database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val email = varchar("email", length = 50)
        val state = integer(name = "state").default(1)
        val demo_completed = bool(name = "demo_completed").default(false)
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
//            if (Users.columns.any { it.name == "age" }) {
//                exec("alter table ${Users.tableName} drop column age")
//            }
            /*
            * TODO: Exposed has issues with checking or existence of columns
            *  so use raw sql query for this.
            * */
            if (Users.exists()) {
                exec("alter table ${Users.tableName} drop column if exists age")
                if (Users.columns.none { it.name == "email" }) {
                    exec("alter table ${Users.tableName} add column email VARCHAR(50)")
                }
                if (Users.columns.none { it.name == "state" }) {
                    exec("alter table ${Users.tableName} add column state")
                }
                if (Users.columns.none { it.name == "demo_completed" }) {
                    exec("alter table ${Users.tableName} add column demo_completed")
                }
            }
            SchemaUtils.createMissingTablesAndColumns(Users)
        }
    }

    //related User operations
    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
    suspend fun create(user: ExposedUser): Int = dbQuery {
        Users.insert {
            it[name] = user.name
            it[email] = user.email
        }[Users.id]
    }

    suspend fun readEmail(email: String): ExposedUserWithId? {
        return dbQuery {
            Users.selectAll()
                .where { Users.email eq email }
                .map {
                    ExposedUserWithId(
                        it[Users.id], it[Users.name], it[Users.email], it[Users.demo_completed]
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun readUserProfileId(id: Int): ExposedUserWithId? {
        return dbQuery {
            Users.selectAll()
                .where { Users.id eq id }
                .map {
                    ExposedUserWithId(
                        it[Users.id], it[Users.name], it[Users.email], it[Users.demo_completed]
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, user: ExposedUser) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[email] = user.email
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }
}