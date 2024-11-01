package org.alwinsden.remnant

import io.ktor.server.config.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import org.alwinsden.remnant.api_data_class.GCloudAuthResult
import org.alwinsden.remnant.models.User.ExposedUser
import org.alwinsden.remnant.models.User.UserSchemaService
import org.alwinsden.remnant.models.configureDatabase

class Authenticator(private val applicationConfig: ApplicationConfig) {

    fun generalAuthenticator(accessToken: String, authMachine: String) {
        if (authMachine == "DESKTOP") {
            var res = desktopAuth(accessToken = accessToken)
        } else if (authMachine == "ANDROID") {
            androidAuth(accessToken = accessToken)
        }
    }

    private fun desktopAuth(accessToken: String): Boolean {
        val client = OkHttpClient()
        val database = configureDatabase(applicationConfig)
        val userInstance = UserSchemaService(database)
        val request = Request.Builder()
            .url("https://www.googleapis.com/oauth2/v3/userinfo")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                //convert it to the GCloudAuthResult type.
                val userDetails = responseBody?.let {
                    Json.decodeFromString<GCloudAuthResult>(it)
                }
                return runBlocking {
                    val userExists = userDetails?.let { userInstance.readEmail(it.email) }

                    //adds the user if the user doesn't exist in the Remnant user DB.
                    if (userExists == null && userDetails != null) {
                        userInstance.create(
                            ExposedUser(
                                email = userDetails.email, name = userDetails.name
                            )
                        )
                        true
                    } else if (userExists != null) {
                        println("The user exists in the database.")
                        true
                    } else {
                        false
                    }
                }
            } else {
                println("Failed to verify the authenticity of the user.")
            }
        }
        return false
    }

    private fun androidAuth(accessToken: String) {

    }
}
