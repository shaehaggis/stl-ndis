package com.example.stl_ndis.data.repositories

import com.example.stl_ndis.data.models.LoginCredentials
import io.github.jan.supabase.SupabaseClient
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {

    fun authenticateWithSupabase(credentials: LoginCredentials): Boolean {
        return true
    }

}