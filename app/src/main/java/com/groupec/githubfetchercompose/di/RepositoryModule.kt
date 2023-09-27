package com.groupec.githubfetchercompose.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.groupec.githubfetchercompose.data.repositories.SampleRepository
import com.groupec.githubfetchercompose.domain.repositories.ISampleRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule  {
    @Singleton
    @Binds
    abstract fun bindApplicationsRepository(impl: SampleRepository): ISampleRepository
}