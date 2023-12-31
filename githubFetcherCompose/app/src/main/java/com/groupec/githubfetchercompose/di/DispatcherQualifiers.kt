package com.groupec.githubfetchercompose.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DispatcherMain

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DispatcherIo

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DispatcherDefault