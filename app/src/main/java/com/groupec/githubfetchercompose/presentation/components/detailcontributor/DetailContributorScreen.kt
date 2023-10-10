package com.groupec.githubfetchercompose.presentation.components.detailcontributor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.groupec.githubfetchercompose.R
import com.groupec.githubfetchercompose.data.dto.ContributorDTO
import com.groupec.githubfetchercompose.presentation.activities.BaseScreen
import com.groupec.githubfetchercompose.presentation.theme.MainTheme
import com.groupec.githubfetchercompose.presentation.utils.SampleButton


class DetailContributorScreen : BaseScreen<DetailContributorScreen.Params>() {
    private var viewModel: DetailContributorViewModel? = null
    private var contributor: ContributorDTO? = null
    private var onBack: (() -> Unit)? = null

    // All variables

    override fun setParameters(params: Params?) {
        params?.let {
            contributor = it.contributor
            onBack = it.onBack
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(contributor?.avatar_url)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.baseline_account_box_24),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                // .border(2.dp, Color.Gray, CircleShape),
                // colorFilter = ColorFilter.tint(Color.Blue)
            )
            contributor?.login?.let { Text(it, style = MaterialTheme.typography.headlineMedium)}
            SampleButton(
                onClick = {
                     onBack?.let { it() }
                },
                text = "Close"
            )


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

    }

    data class Params(val contributor: ContributorDTO, val onBack: () -> Unit)
}