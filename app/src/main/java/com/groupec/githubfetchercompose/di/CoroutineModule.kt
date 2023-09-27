package com.groupec.githubfetchercompose.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
object CoroutineModule {

    @DispatcherMain
    @Provides
    fun provideDispatcherMain() = Dispatchers.Main

    @DispatcherIo
    @Provides
    fun provideDispatcherIo() = Dispatchers.IO

    @DispatcherDefault
    @Provides
    fun provideDispatcherDefault() = Dispatchers.Default
}