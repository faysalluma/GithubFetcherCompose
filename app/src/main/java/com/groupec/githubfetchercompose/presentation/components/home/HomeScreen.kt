package com.groupec.githubfetchercompose.presentation.components.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.groupec.githubfetchercompose.R
import com.groupec.githubfetchercompose.data.dto.RepositoryDTO
import com.groupec.githubfetchercompose.presentation.activities.BaseScreen
import com.groupec.githubfetchercompose.presentation.theme.MainTheme
import com.groupec.githubfetchercompose.utils.Failure
import com.groupec.githubfetchercompose.utils.components.SearchBarListOut


class HomeScreen : BaseScreen<HomeScreen.Params>() {
    private var viewModel: HomeViewModel? = null
    private var onNext: ((RepositoryDTO) -> Unit)? = null

    // All variables

    override fun setParameters(params: Params?) {
        params?.let {
            onNext = it.onNext
        }
    }

    @Composable
    override fun InitViewModel() {
        this.viewModel = hiltViewModel()
    }

    @Composable
    override fun ScreenInternal() {

        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        // Mutable vars
        val loadingItems = remember { mutableStateOf(true) }

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
            contentAlignment = Alignment.Center
        ) {
            // Response viewModels
            viewModel?.repositories?.observeAsState()?.value?.let { repositoryDTOItems ->
                loadingItems.value = false
                SearchBarListOut(items = repositoryDTOItems){ repository ->
                    onNext?.let { it(repository) }
                }
            }

            if (loadingItems.value) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary
                )
            }
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MainTheme {
            Surface(Modifier.fillMaxSize()) {
                ScreenInternal()
            }
        }
    }

    override fun screenLifeCycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                // Get repositories List
                viewModel?.getRepositoryList()
            }

            else -> null
        }
    }

    data class Params(val onNext: ((RepositoryDTO) -> Unit))
}