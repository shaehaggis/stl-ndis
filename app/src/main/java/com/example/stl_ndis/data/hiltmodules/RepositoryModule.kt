package com.example.stl_ndis.data.hiltmodules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.stl_ndis.core.STLNDISApplication
import com.example.stl_ndis.data.repositories.JobRepository
import com.example.stl_ndis.data.repositories.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesSupabaseClient(application: Application): SupabaseClient{
        val stlNDISApplication = application as STLNDISApplication
        return stlNDISApplication.client
    }

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val app = context as STLNDISApplication
        return app.getSharedPreferences()
    }

    @Provides
    @Singleton
    fun providesLoginRepository(supabaseClient: SupabaseClient, dataStore: DataStore<Preferences>): LoginRepository {
        return LoginRepository(supabaseClient, dataStore)
    }

    @Provides
    @Singleton
    fun providesJobRepository(supabaseClient: SupabaseClient): JobRepository {
        return JobRepository(supabaseClient)
    }
}