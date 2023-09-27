package com.groupec.famocopayapp2apptoolscompose.presentation.components.home

import androidx.lifecycle.*
import com.groupec.githubfetchercompose.di.DispatcherDefault
import com.groupec.githubfetchercompose.domain.usecases.init.ValidModeStateUseCase
import com.groupec.githubfetchercompose.domain.usecases.init.ValidModeTaskUseCase
import com.groupec.githubfetchercompose.utils.Failure
import com.groupec.githubfetchercompose.utils.right
import com.groupec.githubfetchercompose.domain.usecases.init.SaveConfigTaskUseCase
import com.groupec.famocopayapp2apptoolscompose.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val validModeTaskUseCase: ValidModeTaskUseCase,
    private val saveConfigTaskUseCase: SaveConfigTaskUseCase,
    private val validModeStateUseCase: ValidModeStateUseCase,
    @DispatcherDefault defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    // Livedatas
    private val _error = MutableLiveData<Failure?>(null)
    val error: LiveData<Failure?> = _error

    private val _saved = SingleLiveEvent<Boolean?>(null)
    var saved: MutableLiveData<Boolean?> = _saved

    // Data Flows
    val stop = Transformations.map(validModeStateUseCase.observe(viewModelScope.coroutineContext + defaultDispatcher)) { it?.right() }

    // Process
    fun stopProcess(value: Boolean) {
        validModeTaskUseCase.execute(viewModelScope, value) {
            it.apply(::handleFailure, ::handleSampleRetrieved)
        }
    }

    fun saveConfig(packag: String, mode: String) {
        saveConfigTaskUseCase.execute(
            viewModelScope,
            SaveConfigTaskUseCase.Settings(packag, mode)
        ) {
            it.apply(::handleFailure, ::handleSavedRetrieved)
        }
    }

    private fun handleSavedRetrieved(retrieved: Boolean) {
        saved.postValue(retrieved)
    }

    private fun handleSampleRetrieved(retrieved: Boolean) {
        // nothing to do
    }

    private fun handleFailure(failure: Failure?) {
        _error.postValue(failure)
    }
}