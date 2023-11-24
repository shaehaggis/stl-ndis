package com.example.stl_ndis.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import com.example.stl_ndis.BuildConfig

@HiltAndroidApp
class STLNDISApplication : Application() {
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
    }
}