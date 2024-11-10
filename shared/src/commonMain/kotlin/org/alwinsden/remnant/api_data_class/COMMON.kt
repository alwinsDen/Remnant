package org.alwinsden.remnant.api_data_class

import kotlinx.serialization.Serializable

@Serializable
data class GCloudAuthResult(
    val sub: String,
    val name: String,
    val given_name: String,
    val family_name: String,
    val picture: String,
    val email: String,
    val email_verified: Boolean,
)

@Serializable
data class ExposedUser(val name: String, val email: String, val demo_completed: Boolean? = false)

@Serializable
data class ExposedUserWithId(
    val id: Int,
    val name: String,
    val email: String,
    val state: Int,
    val gender: String,
    val city: String? = null,
    val demo_completed: Boolean? = false
)

@Serializable
data class ResponseMessage(
    val message: String,
)