package com.example.stl_ndis.core

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import com.example.stl_ndis.BuildConfig
import io.github.jan.supabase.gotrue.GoTrue

@HiltAndroidApp
class STLNDISApplication : Application() {
    val client by lazy {
        getSharedPreferences()
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Postgrest)
            install(GoTrue)
        }
    }

    fun getSharedPreferences(): SharedPreferences {
        return getSharedPreferences("STLNDISPreferences", Context.MODE_PRIVATE)
    }
}