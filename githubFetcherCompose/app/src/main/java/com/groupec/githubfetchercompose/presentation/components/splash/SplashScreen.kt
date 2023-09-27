package com.groupec.githubfetchercompose.presentation.components.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.groupec.githubfetchercompose.presentation.activities.BaseScreen
import com.groupec.githubfetchercompose.presentation.theme.LightGreen
import com.groupec.githubfetchercompose.presentation.theme.MainTheme
import com.groupec.githubfetchercompose.presentation.theme.Silver
import com.groupec.githubfetchercompose.presentation.utils.AlertInfoDialog
import com.groupec.githubfetchercompose.R


class SplashScreen : BaseScreen<SplashScreen.Params>() {
    var viewModel: SplashViewModel? = null
    var onNextHome: (() -> Unit)? = null
    var onNextOperation: (() -> Unit)? = null
    var packagesTab = listOf(Packages.DEMO.value, Packages.RECETTE.value, Packages.PROD.value)

    override fun setParameters(params: Params?) {
        params?.let {
            onNextHome = it.onNextHome
            onNextOperation = it.onNextOperation
        }
    }

    @Composable
    override fun InitViewModel() {
        this.viewModel = hiltViewModel()
        // Check if famoco ContactLess is installed
        viewModel?.checkContactLessInstalled(packagesTab)

    }

    @Composable
    override fun ScreenInternal() {

        viewModel?.checked?.observeAsState()?.value?.let {
            if (it)
                viewModel?.init()
            else
                AlertInfoDialog(
                    title = null,
                    icon = painterResource(id = R.drawable.ic_error),
                    message = stringResource(id = R.string.message_no_contactless_installed),
                    textButton = stringResource(id = R.string.close),
                    closing = this.activity
                )
        }

        // Go to next
        viewModel?.ready?.observeAsState()?.value?.let { configAlreadySaved ->
            LaunchedEffect(true) {
                if (configAlreadySaved)
                    onNextOperation?.let { it() }
                else
                    onNextHome?.let { it() }

            }
        }

        viewModel?.error?.observeAsState()?.value.let {
            // nothing to do
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Silver),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_famoco),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.app_name_title),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            CircularProgressIndicator(
                modifier = Modifier.padding(10.dp),
                color = LightGreen
            )
        }
    }

    @Preview(showBackground = true, widthDp = 320, heightDp = 420)
    @Composable
    fun DefaultPreview() {
        MainTheme {
            ScreenInternal()
            /*AlertInfoDialog(
                title = null,
                icon = painterResource(id = R.drawable.ic_error),
                message = stringResource(id = R.string.message_no_contactless_installed),
                textButton = stringResource(id = R.string.close),
                closing = this.activity
            )*/
        }
    }

    override fun screenLifeCycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {

    }

    data class Params(val onNextHome: () -> Unit, val onNextOperation: () -> Unit)
}