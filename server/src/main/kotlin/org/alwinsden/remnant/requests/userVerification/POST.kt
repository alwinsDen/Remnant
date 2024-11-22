package org.alwinsden.remnant.requests.userVerification

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.alwinsden.remnant.api_data_class.ResponseMessage
import org.alwinsden.remnant.api_data_class.UserBasicDetails
import org.alwinsden.remnant.models.User.UserSchemaService
import org.jetbrains.exposed.sql.Database

class UserVerificationPOST(private val database: Database) {

    private val userInstance = UserSchemaService(database)

    suspend fun Route.postUserBasicDetails() {
        post("/basic_user_details") {
            val req = call.receive<UserBasicDetails>()
            val userInfo = getUserInfo(call)
            call.respond(
                HttpStatusCode.Accepted,
                ResponseMessage("basic details updated.")
            )
        }
    }
}