package org.alwinsden.remnant.networking_utils

sealed class NetworkLogCodes(val code: String) {
    object SuccessPing : NetworkLogCodes("SUCCESSFUL_NETWORK_CALL")
    object FailedPing : NetworkLogCodes("FAILED_NETWORK_CALL")
    object FailedAuth : NetworkLogCodes("FAILED_OBTAINING_AUTH")
    object ObtainedAuth : NetworkLogCodes("OBTAINED_AUTH")
}