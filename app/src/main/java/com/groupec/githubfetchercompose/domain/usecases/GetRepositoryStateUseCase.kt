package com.groupec.githubfetchercompose.domain.usecases

import com.groupec.githubfetchercompose.data.dto.RepositoryDTO
import com.groupec.githubfetchercompose.domain.repositories.ISampleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRepositoryStateUseCase @Inject constructor(private val sampleRepository: ISampleRepository) : StateUseCase<List<RepositoryDTO>>() {
    override fun provideState() = sampleRepository.repositories
}