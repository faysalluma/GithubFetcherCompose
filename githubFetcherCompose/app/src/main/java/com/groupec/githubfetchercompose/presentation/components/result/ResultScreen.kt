package com.groupec.githubfetchercompose.presentation.components.result

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.groupec.githubfetchercompose.R
import com.groupec.githubfetchercompose.presentation.activities.BaseScreen
import com.groupec.githubfetchercompose.presentation.theme.Green
import com.groupec.githubfetchercompose.presentation.theme.MainTheme
import com.groupec.githubfetchercompose.presentation.theme.Red
import com.groupec.githubfetchercompose.presentation.utils.SampleButton
import com.groupec.githubfetchercompose.utils.getError
import com.groupec.famocopayapp2apptoolscompose.presentation.components.home.HomeViewModel
import androidx.compose.material3.MaterialTheme


class ResultScreen : BaseScreen<ResultScreen.Params>() {
    var viewModel: HomeViewModel? = null
    var onBack: (() -> Unit)? = null
    var result: ResultDTO? = null
    var transaction: TransactionDTO? = null

    override fun setParameters(params: Params?) {
        params?.let {
            result = it.result
            transaction = it.transaction
            onBack = it.onBack
        }
    }

    @Composable
    override fun InitViewModel() {
        this.viewModel = hiltViewModel()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun ScreenInternal() {

        var resultImage: Int = 0
        var resultColor: Color? = null
        var resultText = ""
        var titleZoneVisibility = false
        var causeZoneVisibility = false
        var resolutionZoneVisibility = false
        var descVisibility = false
        var title = ""
        var desc = ""
        var cause = ""
        var resolution = ""

        result?.let {
            if (it.success) {
                resultImage = R.drawable.ic_check
                resultText = stringResource(R.string.success)
                resultColor = Green
                titleZoneVisibility = false
                causeZoneVisibility = false
                resolutionZoneVisibility = false
                descVisibility = true
                desc = stringResource(R.string.attestation_success)
            } else {
                resultImage = R.drawable.ic_error
                resultText = stringResource(R.string.error)
                resultColor = Red
                titleZoneVisibility = true
                causeZoneVisibility = true
                resolutionZoneVisibility = true
                descVisibility = true
                title = ((it.error?.getError())?.title ?: it.error) as String
                desc = ((it.error?.getError())?.details ?: it.message) as String
                cause = (it.error?.getError())?.cause ?: stringResource(R.string.none)
                resolution = (it.error?.getError())?.resolution ?: stringResource(R.string.none)
            }
        }

        transaction?.let {
            causeZoneVisibility = false
            resolutionZoneVisibility = false
            if (it.resultCode == Activity.RESULT_OK) {
                resultImage = R.drawable.ic_check
                resultText = stringResource(R.string.success)
                resultColor = Green
                titleZoneVisibility = false
                descVisibility = true
                desc = it.fullReceipt.toString()
            } else {
                resultImage = R.drawable.ic_error
                resultText = stringResource(R.string.error)
                resultColor = Red
                if (it.errorCode != null) {
                    titleZoneVisibility = true
                    title = stringResource(
                        R.string.title_view,
                        it.errorCode.toString(),
                        it.errorName.toString()
                    )
                } else {
                    titleZoneVisibility = false
                }
                descVisibility = true
                desc = it.errorMessage.toString()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = resultImage),
                contentDescription = null,
                contentScale = ContentScale.Crop, // crop the image if it's not a square
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(120.dp)
                    .clip(CircleShape)                       // clip to the circle shape
                    .border(1.dp, Green, CircleShape)
            )

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f)
                    .fillMaxWidth()
            ) {

                Text(
                    text = resultText,
                    color = resultColor!!,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(bottom = 28.dp),
                    fontWeight = FontWeight.Bold
                )

                if (titleZoneVisibility) {
                    Row(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(
                            text = stringResource(R.string.title),
                            modifier = Modifier.padding(end = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(title, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                if (descVisibility) {
                    Row(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(
                            text = stringResource(R.string.details),
                            modifier = Modifier.padding(end = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(desc, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                if (causeZoneVisibility) {
                    Row(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(
                            text = stringResource(R.string.common_causes),
                            modifier = Modifier.padding(end = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(cause, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                if (resolutionZoneVisibility) {
                    Row(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(
                            text = stringResource(R.string.resolution_steps),
                            modifier = Modifier.padding(end = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(resolution, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Box(
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SampleButton(onClick = {
                        onBack?.let { it() }
                    }, text = stringResource(R.string.close))
                }
            }

        }
    }

    @Preview(showBackground = true, widthDp = 320, heightDp = 500)
    @Composable
    fun DefaultPreview() {
        MainTheme {
            ScreenInternal()
        }
    }

    override fun screenLifeCycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {

    }

    data class Params(
        val result: ResultDTO?,
        val transaction: TransactionDTO?,
        val onBack: (() -> Unit)
    )
}