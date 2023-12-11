package com.example.stl_ndis.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.stl_ndis.data.datastore.UserSettingsKeys
import com.example.stl_ndis.data.helpers.SupabaseClientWrapper
import com.example.stl_ndis.data.models.LoginCredentials
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val supabaseClientWrapper: SupabaseClientWrapper,
    private val dataStore: DataStore<Preferences>
) {

    suspend fun authenticateWithSupabase(credentials: LoginCredentials): Boolean {

        return try {
            supabaseClientWrapper.login(credentials)

            val userInfo = supabaseClientWrapper.getUserInfo()

            saveUserInfoToDataStore(userInfo.firstName, userInfo.surname, userInfo.role)

            true
        } catch (e: Exception) {
            println(e)
            false
        }

    }

    private suspend fun saveUserInfoToDataStore(firstName: String, surname: String, role: String) {
        dataStore.edit { preferences ->
            preferences[UserSettingsKeys.FIRST_NAME_KEY] = firstName
            preferences[UserSettingsKeys.SURNAME_KEY] = surname
            preferences[UserSettingsKeys.ROLE_KEY] = role
        }
    }

}