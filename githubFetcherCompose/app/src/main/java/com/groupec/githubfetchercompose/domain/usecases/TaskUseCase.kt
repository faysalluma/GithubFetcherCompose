package com.groupec.githubfetchercompose.domain.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import com.groupec.githubfetchercompose.utils.Either
import com.groupec.githubfetchercompose.utils.Failure

abstract class TaskUseCase<out T: Any?, in P> {
    protected abstract suspend fun run(param: P): Either<Failure, T>

    fun execute(scope: CoroutineScope, param: P, f : (Either<Failure, T>) -> Unit) {
        scope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                run(param)
            }
            f(deferred.await())
        }
    }
}