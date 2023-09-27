package com.groupec.githubfetchercompose.domain.usecases

import com.groupec.githubfetchercompose.domain.repositories.ISampleRepository
import com.groupec.githubfetchercompose.utils.Either
import com.groupec.githubfetchercompose.utils.Failure
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetBranchTaskUseCase @Inject constructor(private val sampleRepository: ISampleRepository) : TaskUseCase<Boolean, Unit>() {
    override suspend fun run(param: Unit): Either<Failure, Boolean> {
        return sampleRepository.getRepositoryBranches()
    }
}