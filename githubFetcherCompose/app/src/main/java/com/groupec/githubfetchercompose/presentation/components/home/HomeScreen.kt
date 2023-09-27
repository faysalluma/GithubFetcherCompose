package com.groupec.famocopayapp2apptoolscompose.presentation.components.home

import Spinner
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.groupec.githubfetchercompose.R
import com.groupec.githubfetchercompose.presentation.activities.BaseScreen
import com.groupec.githubfetchercompose.presentation.theme.MainTheme
import com.groupec.githubfetchercompose.presentation.utils.ClickButton
import com.groupec.githubfetchercompose.utils.Failure
import kotlinx.coroutines.launch

class HomeScreen : BaseScreen<HomeScreen.Params>() {
    var viewModel: HomeViewModel? = null
    var onNext: (() -> Unit)? = null

    // Tab of Packages, Mode
    var packagesTab =
        listOf(Packages.DEMO.toString(), Packages.RECETTE.toString(), Packages.PROD.toString())
    var modeTab = listOf(Mode.WoSSO.toString(), Mode.SSO.toString())

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

        Log.i("TAG", "ScreenInternal set: ")

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            var selectedPackage by remember { mutableStateOf(packagesTab[0]) }
            var selectedMode by remember { mutableStateOf(modeTab[0]) }

            // Observe Data's
            val stop = viewModel?.stop?.observeAsState()?.value?:false

            viewModel?.saved?.observeAsState()?.value?.let {
                LaunchedEffect(Unit)
                {
                    onNext?.let { it() }
                }
            }

            viewModel?.error?.observeAsState()?.value?.let {
                val text = when (it) {
                    is Failure.NetworkFailure -> it.msg
                    is Failure.FeatureFailure -> "Feature Failure"
                    else -> ""
                }
                if (! text.isEmpty()){
                    LaunchedEffect(Unit)
                    {
                        scope.launch {
                            Toast.makeText(context, context.resources.getString(R.string.sso_not_available), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.choose_config),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Row(modifier = Modifier.padding(top = 20.dp)) {
                    Text(
                        text = stringResource(R.string.package_title),
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .weight(.5f)
                            .padding(top = 8.dp)
                    )

                    Surface(modifier = Modifier.weight(.5f)) {
                        MySpinner(
                            items = packagesTab,
                            selectedItem = selectedPackage
                        ) {
                            selectedPackage = it
                        }
                    }
                }
                Row(modifier = Modifier.padding(bottom = 10.dp)) {
                    Text(
                        text = stringResource(R.string.mode_title),
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .weight(.5f)
                            .padding(top = 8.dp)
                    )

                    Surface(modifier = Modifier.weight(.5f)) {
                        MySpinner(
                            items = modeTab,
                            selectedItem = selectedMode
                        ) { mode ->
                            selectedMode = mode
                            if (mode == Mode.SSO.toString())
                                viewModel?.stopProcess(true)
                            else
                                viewModel?.stopProcess(false)
                        }
                    }
                }
                ClickButton(
                    onClick = {
                        // if Mode SSO found
                        if (stop){
                            scope.launch {
                                Toast.makeText(context, context.resources.getString(R.string.sso_not_available), Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            viewModel?.saveConfig(selectedPackage, selectedMode)
                        }
                    },
                    text = stringResource(id = R.string.next)
                )
            }
        }
    }

    @Composable
    fun MySpinner(
        items: List<String>,
        selectedItem: String,
        onItemSelected: (String) -> Unit
    ) {
        Spinner(
            modifier = Modifier.wrapContentSize(),
            dropDownModifier = Modifier.wrapContentSize(),
            items = items,
            selectedItem = selectedItem,
            onItemSelected = onItemSelected,
            selectedItemFactory = { modifier, item ->
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = modifier
                        .padding(8.dp)
                        .wrapContentSize()
                ) {
                    Text(item, modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                        contentDescription = "drop down arrow"
                    )
                }
            },
            dropdownItemFactory = { item, _ ->
                Text(text = item)
            }
        )
    }

    @Preview(showBackground = true, widthDp = 320, heightDp = 420)
    @Composable
    fun DefaultPreview() {
        MainTheme {
            ScreenInternal()
        }
    }

    override fun screenLifeCycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {

    }

    data class Params(val onNext: (() -> Unit))
}