package com.example.stl_ndis.ui.state

sealed class SaveJobStatus {
    object Idle : SaveJobStatus()
    object Success : SaveJobStatus()
    object Failure : SaveJobStatus()
    object Loading : SaveJobStatus()
}
