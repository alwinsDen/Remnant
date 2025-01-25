package org.alwinsden.remnant.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import org.alwinsden.remnant.api_data_class.AuthPost
import org.alwinsden.remnant.api_data_class.ExposedUserWithId
import org.alwinsden.remnant.api_data_class.MessageResponseClass
import org.alwinsden.remnant.api_data_class.ResponseMessage
import org.alwinsden.remnant.api_data_class.UserBasicDetails
import org.alwinsden.remnant.dataStore.coreComponent
import org.alwinsden.remnant.networkHost
import org.alwinsden.remnant.networking_utils.NetworkError
import org.alwinsden.remnant.networking_utils.Result
import javax.naming.AuthenticationException

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

    suspend fun profileGetRequest(): Result<ExposedUserWithId, NetworkError> {
        val jwtTokenValue = coreComponent.appPreferences.doesAuthKeyExist()
        val response: HttpResponse = try {
            httpClient.get(
                "$serverEndPoint/profile",
            ) {
                headers {
                    append("Authorization", "Bearer $jwtTokenValue")
                }

            }
        } catch (e: AuthenticationException) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return if (response.status == HttpStatusCode.Accepted) {
            Result.Success(response.body())
        } else {
            Result.Error(NetworkError.UNAUTHORIZED)
        }
    }

    suspend fun demoCompletedPOSTRequest(): Result<ResponseMessage, NetworkError> {
        val jwtTokenValue = coreComponent.appPreferences.doesAuthKeyExist()
        val response: HttpResponse = try {
            httpClient.post(
                "$serverEndPoint/completeDemo",
            ) {
                headers {
                    append("Authorization", "Bearer $jwtTokenValue")
                }
            }
        } catch (e: AuthenticationException) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return if (response.status == HttpStatusCode.Accepted) {
            Result.Success(response.body())
        } else {
            Result.Error(NetworkError.BAD_REQUEST)
        }
    }

    suspend fun userBasicDetailsRequest(data: UserBasicDetails): Result<ResponseMessage, NetworkError> {
        val jwtTokenValue = coreComponent.appPreferences.doesAuthKeyExist()
        val response: HttpResponse = try {
            httpClient.post(
                "$serverEndPoint/basic_user_details",
            ) {
                headers {
                    append("Authorization", "Bearer $jwtTokenValue")
                }
                contentType(ContentType.Application.Json)
                setBody(
                    UserBasicDetails(
                        gender = data.gender,
                        city = data.city,
                        userAge = data.userAge,
                        workingHrStart = data.workingHrStart,
                        workingMinuteStart = data.workingMinuteStart,
                        workingHrEnd = data.workingHrEnd,
                        workingMinuteEnd = data.workingMinuteEnd,
                        userPrompt = data.userPrompt
                    )
                )
            }
        } catch (e: AuthenticationException) {
            return Result.Error(NetworkError.UNAUTHORIZED)
        }
        return if (response.status == HttpStatusCode.Accepted) {
            Result.Success(response.body())
        } else {
            Result.Error(NetworkError.BAD_REQUEST)
        }
    }
}