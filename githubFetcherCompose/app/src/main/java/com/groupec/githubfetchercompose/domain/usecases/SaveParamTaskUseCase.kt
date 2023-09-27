package com.groupec.githubfetchercompose.domain.usecases

import com.groupec.githubfetchercompose.domain.repositories.ISampleRepository
import com.groupec.githubfetchercompose.utils.Either
import com.groupec.githubfetchercompose.utils.Failure
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveParamTaskUseCase @Inject constructor(private val sampleRepository: ISampleRepository) : TaskUseCase<Boolean, SaveParamTaskUseCase.Fullname>() {
    override suspend fun run(param: Fullname): Either<Failure, Boolean> {
        return sampleRepository.saveFullName(param.reponame, param.username)
    }
    data class Fullname(val reponame: String, val username: String)
}