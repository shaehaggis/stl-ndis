package com.example.stl_ndis.ui.state

sealed class LoginState {
    object Idle: LoginState()
    object Loading: LoginState()
    object Success: LoginState()
    data class Error(val errorMessage: String?): LoginState()
}
