package com.groupec.githubfetchercompose.domain.usecases

import com.groupec.githubfetchercompose.data.dto.ContributorDTO
import com.groupec.githubfetchercompose.domain.repositories.ISampleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetContributorStateUseCase @Inject constructor(private val sampleRepository: ISampleRepository) : StateUseCase<List<ContributorDTO>>() {
    override fun provideState() = sampleRepository.contributors
}