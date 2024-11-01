package org.alwinsden.remnant

import com.auth0.jwk.JwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import org.alwinsden.remnant.api_data_class.ExposedUser
import org.alwinsden.remnant.api_data_class.GCloudAuthResult
import org.alwinsden.remnant.models.User.UserSchemaService
import org.alwinsden.remnant.models.configureDatabase
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

class Authenticator(private val applicationConfig: ApplicationConfig, private val jwkProvider: JwkProvider) {

    fun generalAuthenticator(accessToken: String, authMachine: String): ExposedUser? {
        if (authMachine == "DESKTOP") {
            val userDataHolder = desktopAuth(accessToken = accessToken)
            return userDataHolder
        } else if (authMachine == "ANDROID") {
            androidAuth(accessToken = accessToken)
        }
        return null
    }

    private fun desktopAuth(accessToken: String): ExposedUser? {
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
                        ExposedUser(
                            email = userDetails.email, name = userDetails.name
                        )
                    } else if (userExists != null) {
                        ExposedUser(
                            email = userExists.email, name = userExists.name
                        )
                    } else {
                        null
                    }
                }
            } else {
                println("Failed to verify the authenticity of the user.")
            }
        }
        return null
    }

    private fun androidAuth(accessToken: String) {

    }

    fun generateJWT(email: String, name: String): String {
        val privateKeyString = applicationConfig.property("jwt.privateKey").getString()
        val audience = applicationConfig.property("jwt.audience").getString()
        val issuer = applicationConfig.property("jwt.issuer").getString()
        //the below ID needs to match with kid param in certs/jwks.json
        val publicKey = jwkProvider.get("86e1621b-31d1-4e10-a9f4-efc72f259180").publicKey
        val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
        val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)
        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("email", email)
            .withClaim("name", name)
            .withExpiresAt(Date(System.currentTimeMillis() + 600000))
            .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
        return token
    }
}
