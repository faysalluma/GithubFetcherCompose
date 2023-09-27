package com.groupec.githubfetchercompose.di

import android.content.Context
import com.groupec.githubfetchercompose.data.database.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule  {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.buildDatabase(context)
    }

    @Singleton
    @Provides
    fun provideSampleDao(appDataBase: AppDataBase) = appDataBase.repositoryDao
}
