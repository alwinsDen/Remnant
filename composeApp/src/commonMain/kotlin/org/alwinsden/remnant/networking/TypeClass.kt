package org.alwinsden.remnant.networking

import kotlinx.serialization.Serializable

@Serializable
data class AuthPost(
    val authCode: String,
    val authMachine: String
)

@Serializable
data class MessageResponseClass(
    val responseCode: Int,
    val responseMessage: String,
)


