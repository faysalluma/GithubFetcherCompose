package com.groupec.githubfetchercompose.domain.usecases

import com.groupec.githubfetchercompose.data.dto.RepositoryDTO
import com.groupec.githubfetchercompose.data.dtole.BranchDTO
import com.groupec.githubfetchercompose.domain.repositories.ISampleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetBranchStateUseCase @Inject constructor(private val sampleRepository: ISampleRepository) : StateUseCase<List<BranchDTO>>() {
    override fun provideState() = sampleRepository.branches
}