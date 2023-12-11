package com.example.stl_ndis.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.stl_ndis.data.datastore.UserSettingsKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun fetchFirstName(): StateFlow<String> {
        val firstNameFlow = dataStore.data.map { preferences ->
            preferences[UserSettingsKeys.FIRST_NAME_KEY] ?: ""
        }

        return firstNameFlow.stateIn(scope = CoroutineScope(Dispatchers.IO))
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}