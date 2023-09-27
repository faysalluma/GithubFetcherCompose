package com.groupec.githubfetchercompose.data.repositories

import android.content.*
import android.util.Log
import com.groupec.githubfetchercompose.data.database.dao.RepositoryDao
import com.groupec.githubfetchercompose.data.datastore.DataStoreManager
import com.groupec.githubfetchercompose.data.dto.ContributorDTO
import com.groupec.githubfetchercompose.data.dto.RepositoryDTO
import com.groupec.githubfetchercompose.data.dtole.BranchDTO
import com.groupec.githubfetchercompose.data.network.SampleApiInterface
import com.groupec.githubfetchercompose.data.network.adapters.asDto
import com.groupec.githubfetchercompose.data.network.bodies.results.Branch
import com.groupec.githubfetchercompose.data.network.bodies.results.Contributor
import com.groupec.githubfetchercompose.data.network.bodies.results.Repository
import com.groupec.githubfetchercompose.domain.repositories.ISampleRepository
import com.groupec.githubfetchercompose.utils.Either
import com.groupec.githubfetchercompose.utils.Failure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("BlockingMethodInNonBlockingContext")
@Singleton
class SampleRepository @Inject constructor(
    private val repositoryDao: RepositoryDao, private val api: SampleApiInterface,
    private val dataStoreManager: DataStoreManager
) : ISampleRepository {
    companion object {
        private val TAG = SampleRepository::class.simpleName
        private const val REQUEST_TIMEOUT = 20000L
    }

    /* State Flow's */
    private val _repositories = MutableStateFlow<Either<Failure, List<RepositoryDTO>>?>(null)
    override val repositories: StateFlow<Either<Failure, List<RepositoryDTO>>?> = _repositories

    private val _contributors = MutableStateFlow<Either<Failure, List<ContributorDTO>>?>(null)
    override val contributors: StateFlow<Either<Failure, List<ContributorDTO>>?> = _contributors

    private val _branches = MutableStateFlow<Either<Failure, List<BranchDTO>>?>(null)
    override val branches: StateFlow<Either<Failure, List<BranchDTO>>?> = _branches

    /* Functions */
    override suspend fun getAllRepositories(): Either<Failure, Boolean> {
        // Search Online
        try {
            val response = api.getAllRepositories().execute()
            if (response.isSuccessful) {
                val repositoriesDTO = (response.body() as List<Repository>).map {
                    it.asDto()
                }
                _repositories.emit(Either.Right(repositoriesDTO))
                return Either.Right(true)
            } else {
                Log.w(TAG, "server error")
                val error = Failure.NetworkFailure("Server error : HTTP CODE ERROR ${response.code()}")
                _repositories.emit(Either.Left(error))
                return Either.Left(error)
            }
        } catch (e: IOException) {
            Log.w(TAG, "an exception occurred: ${e.stackTraceToString()}")
            val error = Failure.NetworkFailure(e.message ?: "")
            _repositories.emit(Either.Left(error))
            return Either.Left(error)
        }
    }

    override suspend fun getRepositoryContributors(): Either<Failure, Boolean> {
        // Check if text is stored in datastore
        val fullname = dataStoreManager.fullNameFlow.firstOrNull()
        if (fullname != null) {
            // Search Online
            try {
                val response = api.getRepositoryContributors(fullname.first.lowercase(), fullname.second.lowercase()).execute()
                if (response.isSuccessful) {
                    Log.i(TAG, "getRepositoryContributors: ${response.body()}")
                    val contributorsDTO = (response.body() as List<Contributor>).map {
                        it.asDto()
                    }
                    _contributors.emit(Either.Right(contributorsDTO))
                    return Either.Right(true)
                } else {
                    Log.w(TAG, "server error")
                    val error = Failure.NetworkFailure("Server error : HTTP CODE ERROR ${response.code()}")
                    _contributors.emit(Either.Left(error))
                    return Either.Left(error)
                }
            } catch (e: IOException) {
                Log.w(TAG, "an exception occurred: ${e.stackTraceToString()}")
                val error = Failure.NetworkFailure(e.message ?: "")
                _contributors.emit(Either.Left(error))
                return Either.Left(error)
            }
        }
        return Either.Left(Failure.NetworkFailure(""))
    }

    override suspend fun getRepositoryBranches(): Either<Failure, Boolean> {
        // Check if text is stored in datastore
        val fullname = dataStoreManager.fullNameFlow.firstOrNull()
        if (fullname != null) {
            // Search Online
            try {
                val response = api.getRepositoryBranches(fullname.first.lowercase(), fullname.second.lowercase()).execute()
                if (response.isSuccessful) {
                    Log.i(TAG, "getRepositoryContributors: ${response.body()}")
                    val contributorsDTO = (response.body() as List<Branch>).map {
                        it.asDto()
                    }
                    _branches.emit(Either.Right(contributorsDTO))
                    return Either.Right(true)
                } else {
                    Log.w(TAG, "server error")
                    val error = Failure.NetworkFailure("Server error : HTTP CODE ERROR ${response.code()}")
                    _branches.emit(Either.Left(error))
                    return Either.Left(error)
                }
            } catch (e: IOException) {
                Log.w(TAG, "an exception occurred: ${e.stackTraceToString()}")
                val error = Failure.NetworkFailure(e.message ?: "")
                _branches.emit(Either.Left(error))
                return Either.Left(error)
            }
        }
        return Either.Left(Failure.NetworkFailure(""))
    }

    override suspend fun saveFullName(reponame: String, username: String): Either<Failure, Boolean> {
        dataStoreManager.setFullName(reponame, username)
        return Either.Right(true)
    }

}