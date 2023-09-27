package com.groupec.githubfetchercompose.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.StateFlow
import com.groupec.githubfetchercompose.utils.Either
import com.groupec.githubfetchercompose.utils.Failure
import kotlin.coroutines.CoroutineContext

abstract class StateUseCase<T> {
    protected abstract fun provideState(): StateFlow<Either<Failure, T>?>
    fun observe(context: CoroutineContext): LiveData<Either<Failure, T>?> = provideState().asLiveData(context)
}