package org.alwinsden.remnant.networking

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.network.*
import kotlinx.serialization.SerializationException
import org.alwinsden.remnant.api_data_class.AuthPost
import org.alwinsden.remnant.api_data_class.MessageResponseClass
import org.alwinsden.remnant.networkHost
import org.alwinsden.remnant.networking_utils.NetworkError
import org.alwinsden.remnant.networking_utils.Result

class ApiCentral(
    private val httpClient: HttpClient
) {
    private val serverEndPoint = "http://${networkHost()}:8080"
    suspend fun testServerStatus(): Result<String, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = serverEndPoint
            )
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when (response.status.value) {
            in 200..299 -> {
                Result.Success(response.body())
            }

            else ->
                Result.Error(NetworkError.UNKNOWN)

        }
    }

    suspend fun authRequest(data: AuthPost): Result<MessageResponseClass, NetworkError> {
        val response: HttpResponse = try {
            httpClient.post(
                "$serverEndPoint/auth",
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    AuthPost(
                        authMachine = data.authMachine,
                        authCode = data.authCode,
                    )
                )
            }
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }
        return when (response.status.value) {
            in 200..299 -> {
                Result.Success(response.body())
            }

            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}