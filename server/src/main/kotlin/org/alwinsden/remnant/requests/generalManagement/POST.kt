package org.alwinsden.remnant.requests.generalManagement

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import okhttp3.Response
import org.alwinsden.remnant.api_data_class.ResponseMessage
import org.alwinsden.remnant.models.User.UserSchemaService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.update

class GeneralManagementPOST(private val database: Database) {
    suspend fun Route.completeDemoState() {
        post("/completeDemo") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()
            val userInstance = UserSchemaService(database)
            println("THIS API WAS ALWAYS CALLED.")
            val userData = userInstance.readUserProfileId(
                id = userId
            ).let {
                if (it == null) {
                    call.respond(
                        HttpStatusCode.Forbidden,
                        ResponseMessage("Invalid user ID")
                    )
                }
                it
            }
            UserSchemaService(database).dbQuery {
                UserSchemaService.Users.update({ UserSchemaService.Users.id eq userData!!.id }) {
                    it[demo_completed] = true
                }
            }
            call.respond(HttpStatusCode.Accepted, ResponseMessage("Updated the demo user state"))
        }
    }
}