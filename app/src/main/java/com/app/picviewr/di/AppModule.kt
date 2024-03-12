package com.app.picviewr.di

import android.content.Context
import com.app.picviewr.api.ApiService
import com.app.picviewr.util.SharedPrefManager
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPrefManager {
        return SharedPrefManager.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideAuthService(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return ApiService.create()
    }
}