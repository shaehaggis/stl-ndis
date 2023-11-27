package com.example.stl_ndis.data.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object UserSettingsKeys {
    val FIRST_NAME_KEY = stringPreferencesKey("first_name")
    val SURNAME_KEY = stringPreferencesKey("surname")
    val ROLE_KEY = stringPreferencesKey("role")
}