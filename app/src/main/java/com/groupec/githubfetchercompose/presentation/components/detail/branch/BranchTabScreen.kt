package com.groupec.githubfetchercompose.presentation.components.detail.branch

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.groupec.githubfetchercompose.R
import com.groupec.githubfetchercompose.presentation.theme.Green
import com.groupec.githubfetchercompose.utils.Failure
import com.groupec.githubfetchercompose.utils.components.BranchList


@Composable
fun BranchTabContent() {
    // Get context and my viewModel
    val context = LocalContext.current
    val viewModel: BranchViewModel = hiltViewModel()

    // Mutable vars
    val loadingItems = remember { mutableStateOf(true) }

    // Get repositories List
    viewModel?.getRepositoryBranchList()

    // ViewModel responses
    viewModel?.error?.observeAsState()?.value?.let {
        loadingItems.value = true
        when (it) {
            is Failure.NetworkFailure -> {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
                }
            }
            is Failure.FeatureFailure -> stringResource(R.string.feature_failure)
            else -> ""
        }
    }

    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = if (loadingItems.value)  Alignment.Center else Alignment.TopStart
    ) {
        // Response viewModels
        viewModel?.branches?.observeAsState()?.value?.let {
            loadingItems.value = false
            BranchList(items = it)
        }

        if (loadingItems.value) {
            CircularProgressIndicator(
                color = Green
            )
        }
    }
}
