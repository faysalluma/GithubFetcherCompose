package com.groupec.githubfetchercompose.domain.repositories

import com.groupec.githubfetchercompose.data.dto.ContributorDTO
import com.groupec.githubfetchercompose.data.dto.RepositoryDTO
import com.groupec.githubfetchercompose.data.dtole.BranchDTO
import com.groupec.githubfetchercompose.utils.Either
import com.groupec.githubfetchercompose.utils.Failure
import kotlinx.coroutines.flow.StateFlow

interface ISampleRepository {
    val repositories: StateFlow<Either<Failure, List<RepositoryDTO>>?>
    val contributors: StateFlow<Either<Failure, List<ContributorDTO>>?>
    val branches: StateFlow<Either<Failure, List<BranchDTO>>?>
    suspend fun getAllRepositories(): Either<Failure, Boolean>
    suspend fun getRepositoryBranches(): Either<Failure, Boolean>
    suspend fun getRepositoryContributors(): Either<Failure, Boolean>
    suspend fun saveFullName(reponame: String, username: String): Either<Failure, Boolean>
}