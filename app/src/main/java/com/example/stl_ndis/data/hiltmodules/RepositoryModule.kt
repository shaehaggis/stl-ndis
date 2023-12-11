package com.example.stl_ndis.data.hiltmodules

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.stl_ndis.data.helpers.SupabaseClientWrapper
import com.example.stl_ndis.data.repositories.JobRepository
import com.example.stl_ndis.data.repositories.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesLoginRepository(supabaseClient: SupabaseClient, dataStore: DataStore<Preferences>): LoginRepository {
        return LoginRepository(supabaseClient, dataStore)
    }

    @Provides
    @Singleton
    fun providesJobRepository(supabaseClientWrapper: SupabaseClientWrapper): JobRepository {
        return JobRepository(supabaseClientWrapper)
    }
}