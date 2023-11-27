package com.example.stl_ndis.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    var showToast: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun showToast(){
        showToast.value = true
    }

    fun hideToast(){
        showToast.value = false
    }
}