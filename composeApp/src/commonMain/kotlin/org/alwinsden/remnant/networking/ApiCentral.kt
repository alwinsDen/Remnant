package org.alwinsden.remnant.networking

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.util.network.*
import kotlinx.serialization.SerializationException
import org.alwinsden.remnant.networkHost
import org.alwinsden.remnant.networking_utils.NetworkError
import org.alwinsden.remnant.networking_utils.Result

class ApiCentral(
    private val httpClient: HttpClient
) {
    suspend fun testServerStatus(): Result<String, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = "http://${networkHost()}:8080"
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
}