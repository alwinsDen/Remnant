package org.alwinsden.remnant.requests.generalManagement

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.alwinsden.remnant.api_data_class.ResponseMessage
import org.alwinsden.remnant.models.User.UserSchemaService
import org.alwinsden.remnant.requests.userVerification.getUserInfo
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.update

class GeneralManagementPOST(private val database: Database) {
    suspend fun Route.completeDemoState() {
        post("/completeDemo") {
            val userInfo = getUserInfo(call);
            val userInstance = UserSchemaService(database)
            val userData = userInstance.readUserProfileId(
                id = userInfo.id
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