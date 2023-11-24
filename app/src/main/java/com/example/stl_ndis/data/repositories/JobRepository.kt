package com.example.stl_ndis.data.repositories

import io.github.jan.supabase.SupabaseClient
import javax.inject.Inject

class JobRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
}