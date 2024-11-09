package org.alwinsden.remnant.models.User

import kotlinx.coroutines.Dispatchers
import org.alwinsden.remnant.api_data_class.ExposedUser
import org.alwinsden.remnant.api_data_class.ExposedUserWithId
import org.alwinsden.remnant.enum_class.GenderEnum
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class UserSchemaService(private val database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val email = varchar("email", length = 50).uniqueIndex()
        val state = integer(name = "state").default(1)
        val gender = integer(name = "gender").default(GenderEnum.NOT_SPECIFIED.value)
        val city = varchar("city", length = 50).nullable()
        val demo_completed = bool(name = "demo_completed").default(false)
        override val primaryKey = PrimaryKey(id)
    }

    fun ResultRow.toExposedUserWithId() = ExposedUserWithId(
        this[Users.id],
        this[Users.name],
        this[Users.email],
        this[Users.state],
        gender = GenderEnum.fromValue(this[Users.gender])!!.displayName,
        this[Users.city],
        this[Users.demo_completed]
    )

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
                if (Users.columns.none { it.name == "gender" }) {
                    exec("alter table ${Users.tableName} add column gender")
                }
                if (Users.columns.none { it.name == "city" }) {
                    exec("alter table ${Users.tableName} add column city")
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

    /**
     * Creates a new user in the database using the provided `ExposedUser` object and returns the newly created user with an ID.
     *
     * @param user The user information to be added, provided as an `ExposedUser` object.
     * @return The newly created user with an ID, returned as an `ExposedUserWithId` object.
     */
    suspend fun create(user: ExposedUser): ExposedUserWithId = dbQuery {
        Users.insert {
            it[name] = user.name
            it[email] = user.email
        }
        Users.selectAll()
            .where { Users.email eq user.email }
            .map { it.toExposedUserWithId() }
            .single()
    }

    suspend fun readEmail(email: String): ExposedUserWithId? {
        return dbQuery {
            Users.selectAll()
                .where { Users.email eq email }
                .map {
                    it.toExposedUserWithId()
                }
                .singleOrNull()
        }
    }

    /**
     * Reads the user profile ID from the database.
     *
     * @param id The ID of the user whose profile is to be read.
     * @return The user profile with the given ID, or null if no such user exists.
     */
    suspend fun readUserProfileId(id: Int): ExposedUserWithId? {
        return dbQuery {
            Users.selectAll()
                .where { Users.id eq id }
                .map {
                    it.toExposedUserWithId()
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