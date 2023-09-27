package com.groupec.githubfetchercompose.presentation.components.detail

import androidx.lifecycle.*
import com.groupec.githubfetchercompose.domain.usecases.SaveParamTaskUseCase
import com.groupec.githubfetchercompose.utils.Failure
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val saveParamTaskUseCase: SaveParamTaskUseCase
) : ViewModel() {

    var savedFullname = MutableLiveData<Boolean>()

    fun saveFullName(reponame: String, username: String) {
        saveParamTaskUseCase.execute(viewModelScope, SaveParamTaskUseCase.Fullname(reponame, username)) {
            it.apply(::handleFailure, ::handleSampleRetrieved)
        }
    }

    private fun handleSampleRetrieved(retrieved: Boolean) {
        savedFullname.postValue(retrieved)
    }

    private fun handleFailure(failure: Failure?) {
        // nothing to do
    }
}