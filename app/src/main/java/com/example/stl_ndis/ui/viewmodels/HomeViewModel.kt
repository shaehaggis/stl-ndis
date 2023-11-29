package com.example.stl_ndis.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stl_ndis.data.datastore.UserSettingsKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
): ViewModel() {
    var showToast: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _firstName = mutableStateOf("")
    val firstName: State<String> = _firstName

    init {
        fetchFirstName()
    }

    private fun fetchFirstName(){
        viewModelScope.launch {
            dataStore.data.map { preferences ->
                preferences[UserSettingsKeys.FIRST_NAME_KEY] ?: ""
            }.collect {firstNameValue ->
                _firstName.value = firstNameValue
            }
        }
    }

    fun clearUserData(){
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences.clear()
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