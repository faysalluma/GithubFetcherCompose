package com.groupec.githubfetchercompose.presentation.components.detail.branch

import androidx.lifecycle.*
import com.groupec.githubfetchercompose.di.DispatcherDefault
import com.groupec.githubfetchercompose.domain.usecases.GetBranchStateUseCase
import com.groupec.githubfetchercompose.domain.usecases.GetBranchTaskUseCase
import com.groupec.githubfetchercompose.utils.Failure
import com.groupec.githubfetchercompose.utils.right
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


@HiltViewModel
class BranchViewModel @Inject constructor(
    private val getBranchTaskUseCase: GetBranchTaskUseCase,
    private val getBranchStateUseCase: GetBranchStateUseCase,
    @DispatcherDefault defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    // Livedatas
    private val _error = MutableLiveData<Failure?>(null)
    val error: LiveData<Failure?> = _error

    // Data Flows
    val branches = getBranchStateUseCase.observe(viewModelScope.coroutineContext+ defaultDispatcher).map { it?.right() }

    // Process
    fun getRepositoryBranchList() {
        getBranchTaskUseCase.execute(viewModelScope, Unit) {
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