package com.example.stl_ndis.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stl_ndis.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    var showToast: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var isLoggingOut: MutableState<Boolean> = mutableStateOf(false)

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName

    init {
        viewModelScope.launch {
            userRepository.fetchFirstName().collect {
                _firstName.value = it
            }
        }
    }

    fun clearUserData(){
        viewModelScope.launch {
            userRepository.clearUserData()
        }
    }

    fun showToast(){
        showToast.value = true
    }

    fun hideToast(){
        showToast.value = false
    }
}