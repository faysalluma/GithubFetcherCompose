package com.groupec.githubfetchercompose.presentation.components.detail.contributor

import androidx.lifecycle.*
import com.groupec.githubfetchercompose.di.DispatcherDefault
import com.groupec.githubfetchercompose.domain.usecases.GetContributorStateUseCase
import com.groupec.githubfetchercompose.domain.usecases.GetContributorTaskUseCase
import com.groupec.githubfetchercompose.utils.Failure
import com.groupec.githubfetchercompose.utils.right
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ContributorViewModel @Inject constructor(
    private val contributorTaskUseCase: GetContributorTaskUseCase,
    private val contributorStateUseCase: GetContributorStateUseCase,
    @DispatcherDefault defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    // Livedatas
    private val _error = MutableLiveData<Failure?>(null)
    val error: LiveData<Failure?> = _error

    // Data Flows
    val contributors = contributorStateUseCase.observe(viewModelScope.coroutineContext+ defaultDispatcher).map{ it?.right() }

    // Process
    fun getRepositoryContributorList() {
        contributorTaskUseCase.execute(viewModelScope, Unit) {
            it.apply(::handleFailure, ::handleSampleRetrieved)
        }
    }

    private fun handleSampleRetrieved(retrieved: Boolean) {
        // nothing to do
    }

    private fun handleFailure(failure: Failure?) {
        _error.postValue(failure)
    }
}