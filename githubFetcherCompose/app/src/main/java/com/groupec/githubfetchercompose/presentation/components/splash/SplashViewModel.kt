package com.groupec.githubfetchercompose.presentation.components.splash

import androidx.lifecycle.*
import com.groupec.githubfetchercompose.domain.usecases.init.CheckInstallTaskUseCase
import com.groupec.githubfetchercompose.utils.Failure
import com.groupec.githubfetchercompose.domain.usecases.init.InitAppTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.groupec.famocopayapp2apptoolscompose.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val initAppTaskUseCase: InitAppTaskUseCase, private val checkInstallTaskUseCase: CheckInstallTaskUseCase) : ViewModel() {
    var checked = MutableLiveData<Boolean>(null)

    private val _ready = SingleLiveEvent<Boolean?>(null)
    var ready: MutableLiveData<Boolean?> = _ready

    private val _error = MutableLiveData<Failure?>(null)
    val error: LiveData<Failure?> = _error

    fun init() {
        initAppTaskUseCase.execute(viewModelScope, Unit) {
            it.apply(::handleFailure, ::handleInitFinished)
        }
    }

    fun checkContactLessInstalled(uri: List<String>) {
        checkInstallTaskUseCase.execute(viewModelScope, uri) {
            it.apply(::handleFailure, ::handleCheckFinished)
        }
    }

    private fun handleInitFinished(value: Boolean) {
        ready.postValue(value)
    }

    private fun handleCheckFinished(value: Boolean) {
        checked.postValue(value)
    }

    private fun handleFailure(failure: Failure?) {
        _error.postValue(failure)
    }
}