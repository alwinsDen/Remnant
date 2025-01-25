package org.alwinsden.remnant.models.User

import kotlinx.coroutines.Dispatchers
import org.alwinsden.remnant.api_data_class.ExposedUser
import org.alwinsden.remnant.api_data_class.ExposedUserWithId
import org.alwinsden.remnant.enum_class.GenderEnum
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

class UserSchemaService(private val database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val email = varchar("email", length = 50).uniqueIndex()
        val state = integer(name = "state").default(1) //this is the page state.
        val gender = integer(name = "gender").default(GenderEnum.NOT_SPECIFIED.value)
        val city = varchar("city", length = 50)
        val user_age = integer("user_age").default(18).check { it greaterEq 18 }
        val working_hr_start = integer("working_hr_start")
            .check { it.between(0, 23) }
            .default(9)
        val working_minute_start = integer("working_minute_start")
            .check { it.between(0, 59) }
            .default(0)
        val working_hr_end = integer("working_hr_end")
            .check { it.between(0, 23) }
            .default(18)
        val working_minute_end = integer("working_minute_end")
            .check { it.between(0, 59) }
            .default(30)
        val user_prompt = varchar("user_prompt", length = 200).default("")
        val demo_completed = bool(name = "demo_completed").default(false)
        val jti_identifier = uuid("jti_identifier").default(UUID.randomUUID())
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
            if (Users.exists()) {
                //TODO: uncomment below to run change execs. WARNED: makes irreversible changes.
                //exec("update ${Users.tableName} set city = 'not selected'")
            }
            //this automatically manages new columns.
            SchemaUtils.createMissingTablesAndColumns(Users)
        }
    }

    //related User operations
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

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

    suspend fun verifyUserExistence(email: String, id: Int, name: String, uuid_token: UUID): Int {
        return dbQuery {
            Users.selectAll()
                .where { (Users.email eq email) and (Users.id eq id) and (Users.jti_identifier eq uuid_token) }
                .map {
                    it.toExposedUserWithId()
                }
                .count()
        }
    }
}