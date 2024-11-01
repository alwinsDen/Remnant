package org.alwinsden.remnant

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import org.alwinsden.remnant.api_data_class.GCloudAuthResult
import org.alwinsden.remnant.models.User.ExposedUser
import org.alwinsden.remnant.models.User.UserSchemaService
import org.alwinsden.remnant.models.configureDatabase
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import java.util.Date
import java.util.concurrent.TimeUnit

fun generalAuthenticator(accessToken: String, authMachine: String, applicationConfig: ApplicationConfig) {
    if (authMachine == "DESKTOP") {
        var res = desktopAuth(accessToken = accessToken, applicationConfig = applicationConfig)
    } else if (authMachine == "ANDROID") {
        androidAuth(accessToken = accessToken)
    }
}

fun desktopAuth(accessToken: String,applicationConfig: ApplicationConfig): Boolean {
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

fun androidAuth(accessToken: String) {

}