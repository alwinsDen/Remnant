package org.alwinsden.remnant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.alwinsden.remnant.dataStore.coreComponent

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

expect fun networkHost(): String

fun returnNavClassOnJwt(): String = runBlocking {
    val jwtTokenValue = coreComponent.appPreferences.doesAuthKeyExist()
    if (jwtTokenValue != "") {
        return@runBlocking NavRouteClass.EntryScreen1.route
    } else {
        return@runBlocking NavRouteClass.Home.route
    }
}