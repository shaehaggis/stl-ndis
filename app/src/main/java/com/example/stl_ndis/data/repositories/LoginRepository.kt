package com.example.stl_ndis.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.stl_ndis.data.datastore.UserSettingsKeys
import com.example.stl_ndis.data.models.LoginCredentials
import com.example.stl_ndis.data.models.UserDTO
import com.example.stl_ndis.data.models.toDomain
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val dataStore: DataStore<Preferences>
) {

    suspend fun authenticateWithSupabase(credentials: LoginCredentials): Boolean {

        return try {
            supabaseClient.gotrue.loginWith(Email) {
                email = credentials.username
                password = credentials.password
            }

            val userId = supabaseClient.gotrue.currentSessionOrNull()?.user?.id

            val response = supabaseClient.postgrest
                .from("users")
                .select {
                    if (userId != null) {
                        eq("id", userId)
                    }
                }
                .decodeSingle<UserDTO>()

            val domainObject = response.toDomain()

            saveUserInfoToDataStore(domainObject.firstName, domainObject.surname, domainObject.role)

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