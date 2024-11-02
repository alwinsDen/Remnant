package org.alwinsden.remnant.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JwtViewModel : ViewModel() {
    private val _tokenState = MutableStateFlow("")
    val tokenState: StateFlow<String> = _tokenState.asStateFlow()

    fun updateToken(token: String) {
        viewModelScope.launch {
            _tokenState.value = token
        }
    }
}