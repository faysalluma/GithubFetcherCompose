package com.groupec.githubfetchercompose.presentation.components.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.groupec.githubfetchercompose.R
import com.groupec.githubfetchercompose.data.dto.ContributorDTO
import com.groupec.githubfetchercompose.data.dto.RepositoryDTO
import com.groupec.githubfetchercompose.presentation.activities.BaseScreen
import com.groupec.githubfetchercompose.presentation.components.detail.branch.BranchTabContent
import com.groupec.githubfetchercompose.presentation.components.detail.contributor.ContributorTabContent

import com.groupec.githubfetchercompose.presentation.theme.Green
import com.groupec.githubfetchercompose.presentation.theme.MainTheme
import kotlinx.coroutines.launch

class DetailScreen : BaseScreen<DetailScreen.Params>() {
    private var viewModel: DetailViewModel? = null
    private var repository: RepositoryDTO? = null
    private var onNext: ((ContributorDTO)-> Unit) ? =  null

    // All variables


    override fun setParameters(params: Params?) {
        params?.let {
            repository = it.repository
            onNext = it.onNext
        }
    }

    @Composable
    override fun InitViewModel() {
        this.viewModel = hiltViewModel()
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun ScreenInternal() {

        // After param saved
        viewModel?.savedFullname?.observeAsState()?.value?.let {
            if (it) {
                // Tabs and tabs content for displaying our page for each tab layout
                val pagerState = rememberPagerState(pageCount = 2) // TabLayout Data size
                Column {
                    // Tabs and tabs content for displaying our page for each tab layout
                    TabLayout(pagerState = pagerState)
                    TabContent(pagerState = pagerState)
                }
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

    @ExperimentalPagerApi
    @Composable
    fun TabLayout(pagerState: PagerState) {
        // List specifying data of tab (name and icon)
        val list = listOf(
            stringResource(R.string.branches) to Icons.Default.Home,
            stringResource(R.string.contributors) to Icons.Default.Settings
        )
        val scope = rememberCoroutineScope()
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Green,
            // contentColor = Color.White,

            // Indicator for the currently selected tab.
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                    height = 2.dp,
                    color = Color.White
                )
            },
            // modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            // we are specifying icon and text for the individual tab item
            list.forEachIndexed { index, _ ->
                Tab(
                    icon = {
                        Icon(
                            imageVector = list[index].second,
                            contentDescription = null,
                            tint = if (pagerState.currentPage == index) Color.White else Color.LightGray
                        )
                    },
                    text = {
                        Text(
                            list[index].first,
                            color = if (pagerState.currentPage == index) Color.White else Color.LightGray
                        )
                    },
                    // on below line we are specifying
                    // the tab which is selected.
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }
    }

    // Display the individual page of our tab
    @ExperimentalPagerApi
    @Composable
    fun TabContent(pagerState: PagerState) {
        HorizontalPager(state = pagerState) { index ->
            when (index) {
                // Calling tab content screen
                0 -> BranchTabContent()
                1 -> ContributorTabContent(onNext)
            }
        }
    }

    override fun screenLifeCycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                // Save args into datastore to get into TabLayout part
                val fullName = repository?.full_name?.split("/")
                fullName?.let {
                    viewModel?.saveFullName(fullName[0], fullName[1])
                }
            }

            else -> null
        }
    }

    data class Params(val repository: RepositoryDTO, val onNext: (ContributorDTO) -> Unit)
}