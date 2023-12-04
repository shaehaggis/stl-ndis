package com.example.stl_ndis.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stl_ndis.data.models.LoginCredentials
import com.example.stl_ndis.data.repositories.JobRepository
import com.example.stl_ndis.data.repositories.LoginRepository
import com.example.stl_ndis.ui.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val jobRepository: JobRepository
): ViewModel() {

    var showToast: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var _username = mutableStateOf("")
    private var _password = mutableStateOf("")

    private var _loadingState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Idle)

    val username: State<String>
        get() = _username

    val password: State<String>
        get() = _password

    val loadingState: MutableStateFlow<LoginState>
        get() = _loadingState

    fun setUsername(newValue: String){
        _username.value = newValue
    }

    fun setPassword(newValue: String){
        _password.value = newValue
    }

    fun login(credentials: LoginCredentials) {
        _loadingState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val response = loginRepository.authenticateWithSupabase(credentials)

                if (response) {
                    _loadingState.value = LoginState.Success
                    setUsername("")
                    setPassword("")
                    jobRepository.initialiseJobs()
                }
                else {
                    _loadingState.value = LoginState.Error("Error authenticating")
                }

            } catch(e: Exception) {
                _loadingState.value = LoginState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun showToast(){
        showToast.value = true
    }

    fun hideToast(){
        showToast.value = false
    }
}