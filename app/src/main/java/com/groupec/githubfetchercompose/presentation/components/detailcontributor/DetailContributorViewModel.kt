package com.groupec.githubfetchercompose.presentation.components.detailcontributor

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
class DetailContributorViewModel @Inject constructor () : ViewModel() {
}