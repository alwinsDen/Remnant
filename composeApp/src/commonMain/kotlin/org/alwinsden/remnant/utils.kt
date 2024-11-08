package org.alwinsden.remnant

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.alwinsden.remnant.dataStore.coreComponent
import org.alwinsden.remnant.networking.ApiCentral
import org.alwinsden.remnant.networking.createHttpClient

open class RemnantAppViewModal : ViewModel() {
    fun launchCatching(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                println("CoroutineExceptionHandler got $throwable")
            },
            block = block
        )
    }
}

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided") }

expect fun networkHost(): String

fun returnNavClassOnJwt(): String = runBlocking {
    val jwtTokenValue = coreComponent.appPreferences.doesAuthKeyExist()
    if (jwtTokenValue != "") {
        return@runBlocking NavRouteClass.EntryScreen1.route
    } else {
        return@runBlocking NavRouteClass.Home.route
    }
}

expect val Screen3PngCoordinates: List<Pair<Float, Float>>

val HTTP_CALL_CLIENT = ApiCentral(createHttpClient(OkHttp.create()))