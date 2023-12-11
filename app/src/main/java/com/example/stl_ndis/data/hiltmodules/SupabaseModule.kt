package com.example.stl_ndis.data.hiltmodules

import android.app.Application
import com.example.stl_ndis.core.STLNDISApplication
import com.example.stl_ndis.data.helpers.SupabaseClientWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SupabaseModule {
    @Provides
    @Singleton
    fun providesSupabaseClient(application: Application): SupabaseClient {
        val stlNDISApplication = application as STLNDISApplication
        return stlNDISApplication.client
    }

    @Provides
    @Singleton
    fun providesSupabaseClientWrapper(supabaseClient: SupabaseClient): SupabaseClientWrapper {
        return SupabaseClientWrapper(supabaseClient)
    }
}