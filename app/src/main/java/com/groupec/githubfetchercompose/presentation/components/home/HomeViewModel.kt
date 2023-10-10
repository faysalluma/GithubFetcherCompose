package com.groupec.githubfetchercompose.presentation.components.home

import androidx.lifecycle.*
import com.groupec.famocopayapp2apptoolscompose.utils.SingleLiveEvent
import com.groupec.githubfetchercompose.di.DispatcherDefault
import com.groupec.githubfetchercompose.domain.usecases.GetRepositoryStateUseCase
import com.groupec.githubfetchercompose.domain.usecases.GetRepositoryTaskUseCase
import com.groupec.githubfetchercompose.utils.Failure
import com.groupec.githubfetchercompose.utils.right
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val getRepositoryTaskUseCase: GetRepositoryTaskUseCase,
    private val getRepositoryStateUseCase: GetRepositoryStateUseCase,
    @DispatcherDefault defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    // Livedatas
    private val _error = SingleLiveEvent<Failure?>(null)
    val error: MutableLiveData<Failure?> = _error

    // Data Flows
    val repositories = getRepositoryStateUseCase.observe(viewModelScope.coroutineContext+ defaultDispatcher).map{ it?.right()}

    // Process
    fun getRepositoryList() {
        getRepositoryTaskUseCase.execute(viewModelScope, Unit) {
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