package org.alwinsden.remnant.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.alwinsden.remnant.NavRouteClass

class JwtViewModel : ViewModel() {
    private val _tokenState = MutableStateFlow("")
    val tokenState: StateFlow<String> = _tokenState.asStateFlow()
    fun updateToken(token: String) {
        viewModelScope.launch {
            _tokenState.value = token
        }
    }
}

class NavControllerViewModel : ViewModel() {
    private val _navRouteState = MutableStateFlow(NavRouteClass.Home.route)
    val navRouteState: StateFlow<String> = _navRouteState.asStateFlow()
    fun updateNavRoute(navRouteClass: String) {
        viewModelScope.launch {
            _navRouteState.value = navRouteClass
        }
    }
}