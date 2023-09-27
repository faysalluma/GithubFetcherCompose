package com.groupec.githubfetchercompose.presentation.components.operation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.groupec.githubfetchercompose.data.Constants
import com.groupec.githubfetchercompose.presentation.activities.BaseScreen
import com.groupec.githubfetchercompose.presentation.theme.LightGreen
import com.groupec.githubfetchercompose.presentation.theme.MainTheme
import com.groupec.githubfetchercompose.presentation.utils.AlertInfoDialog
import com.groupec.githubfetchercompose.presentation.utils.MyPrimaryButton
import com.groupec.githubfetchercompose.presentation.utils.PayDialogConfig
import com.groupec.githubfetchercompose.presentation.utils.RefundDialog
import com.groupec.githubfetchercompose.utils.getPackageValue
import kotlinx.coroutines.launch


class OperationScreen : BaseScreen<OperationScreen.Params>() {
    var viewModel: OperationViewModel? = null
    var onNextResult: ((ResultDTO?, TransactionDTO?) -> Unit)? = null
    var onBackHome: (() -> Unit)? = null

    private var resultTransaction = TransactionDTO()
    private lateinit var listPayment: List<PaymentDTO>
    private var lastPayment: String? = null
    private lateinit var context: Context
    private lateinit var famocopayPackageInstalled: String
    private var packagesList = listOf(Packages.DEMO.value, Packages.RECETTE.value, Packages.PROD.value)
    private lateinit var progressVisibity : MutableState<Boolean>

    override fun setParameters(params: Params?) {
        params?.let {
            onNextResult = it.onNextResult
            onBackHome = it.onBackHome
        }
    }

    @Composable
    override fun InitViewModel() {
        this.viewModel = hiltViewModel()
    }

    @Composable
    override fun ScreenInternal() {
        // For showing SnackBar
        val scope = rememberCoroutineScope()
        context = LocalContext.current.applicationContext

        // To display settings (mode and packag) values
        var mode by remember { mutableStateOf("") }
        var packag by remember { mutableStateOf("") }
        var transacionId by remember { mutableStateOf("") }

        // To show and hide progress view
        progressVisibity = remember { mutableStateOf(false) }
        val lastTransactionVisibility = rememberSaveable { mutableStateOf(false) }

        // create a state variable to keep track of whether the dialog should be shown or not
        val showDialog = rememberSaveable { mutableStateOf(false) }
        val showDialogRefund = rememberSaveable { mutableStateOf(false) }


        // Handle the back button event
        BackHandler {
            this.activity?.finish()
        }

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            // Pay Activity Result
            val myLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->

                resultTransaction.resultCode = result.resultCode
                val data = result.data
                if (result.resultCode == Activity.RESULT_OK && data != null) {
                    // The transaction is successful
                    val userId = data.getIntExtra("userId", -1)
                    val merchantId = data.getIntExtra("merchantId", -1)
                    val terminalId = data.getStringExtra("terminalId") ?: "None"

                    val transactionId = data.getIntExtra("transactionId", -1)

                    val amount = data.getLongExtra("amount", 0)
                    val currency = data.getStringExtra("currency") ?: "None"
                    val date = data.getStringExtra("date") ?: "None"

                    val authorized = data.getBooleanExtra("authorized", false)
                    val declineCause = data.getStringExtra("declineCause")
                    val status = data.getStringExtra("status") ?: "None"

                    val mode = data.getStringExtra("mode") ?: "None"
                    val scheme = data.getStringExtra("scheme") ?: "None"
                    val pan = data.getStringExtra("pan") ?: "None"
                    val aid = data.getStringExtra("aid") ?: "None"
                    val applicationLabel = data.getStringExtra("applicationLabel") ?: "None"
                    val transactionType = data.getStringExtra("transactionType") ?: "None"

                    val authMode = data.getStringExtra("authMode") ?: "None"
                    val authCode = data.getStringExtra("authCode") ?: "None"
                    // val latitude = data.getStringExtra("latitude") ?: "None"
                    // val longitude = data.getStringExtra("longitude") ?: "None"

                    val loyaltyIdentifier = data.getStringExtra("loyaltyIdentifier") ?: "None"
                    val reference = data.getStringExtra("externalReferenceq") ?: "None"

                    val fullReceipt =
                        data.getStringExtra("formatedString") ?: "None" // Full receipt string

                    // Use the transaction result here
                    resultTransaction.fullReceipt = fullReceipt

                    // Save transaction into local database
                    viewModel?.savePayment(transactionId, fullReceipt, date)
                } else if (result.resultCode == Activity.RESULT_CANCELED) {
                    if (data != null) {
                        if (data.getBooleanExtra("helpNeeded", false)) {
                            // The help button was pressed
                            resultTransaction.errorMessage =
                                activity?.getString(R.string.error_help_button)
                        } else {
                            // An error occured
                            val errorCode = data.getStringExtra("errorCode")
                            val errorName = data.getStringExtra("errorName") ?: "None"
                            val errorMessage = data.getStringExtra("errorMessage") ?: "None"
                            resultTransaction.errorCode = errorCode
                            resultTransaction.errorName = errorName
                            resultTransaction.errorMessage = errorMessage
                            if (resultTransaction.errorCode == null) resultTransaction.errorMessage =
                                activity?.getString(R.string.wrong_configuration)
                        }
                    } else {
                        // The transaction have been cancelled
                        resultTransaction.errorMessage =
                            activity?.getString(R.string.error_transaction_canceled)
                    }
                }

                // Go to result screen
                onNextResult?.let { it(null, resultTransaction) }

            }

            // Get Config from DataStore
            viewModel?.getConfig()

            // Get local Order
            viewModel?.getLocalPayment()

            viewModel?.settings?.observeAsState()?.value?.let {
                val detailPackage = it.first.getPackageValue()
                mode = it.second
                packag = stringResource(R.string.package_view, it.first, detailPackage.toString())
            }

            viewModel?.savedPaymentLocal?.observeAsState()?.value?.let {
                LaunchedEffect(Unit)
                {
                    // Toast.makeText(context, context.resources.getString(R.string.paymentLocalSaved), Toast.LENGTH_SHORT).show()
                }
            }

            viewModel?.checked?.observeAsState()?.value?.let {
                if (it)
                    viewModel?.checkFamocopayPackage()
                else
                    AlertInfoDialog(
                        title = null,
                        icon = painterResource(id = R.drawable.ic_error),
                        message = stringResource(id =R.string.message_no_contactless_installed),
                        textButton = stringResource(id = R.string.close),
                        closing = activity
                    )
            }

            viewModel?.checkFamocopayPackage?.observeAsState()?.value?.let {
                famocopayPackageInstalled = it.second
                if (!it.first) {
                    LaunchedEffect(Unit) {
                        onBackHome?.let { it() }
                    }

                    scope.launch {
                        Toast.makeText(
                            context,
                            context.resources.getString(R.string.famocopayInstalledPackageError),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            viewModel?.paymentList?.observeAsState()?.value?.let {
                listPayment = it // To check if list is empty or not in Dialog
                if (!listPayment.isEmpty()) lastPayment = listPayment[0].transactionId.toString()

                // Hide title if list empty
                lastTransactionVisibility.value = !listPayment.isEmpty()

                // Fill transactionId EditText
                if (lastPayment != null) transacionId = lastPayment.toString()

                if (showDialogRefund.value) {
                    RefundDialog(transacionId, lastTransactionVisibility.value,
                        setShowDialog = { showDialogRefund.value = it }, items = it){ transactionId ->
                        val intent = Intent().apply {
                            setClassName(famocopayPackageInstalled, Constants.CLASS_NAME)
                            // putExtra("merchantId", id)
                            putExtra("transactionType", "REFUND")
                            putExtra("transactionType", transactionId)
                            putExtra("externalTransactionReference", "XXXX-1234")
                        }
                        myLauncher.launch(intent)
                    }
                }
            }

            /* Design */
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    ) {

                Text(
                    text = stringResource(R.string.run_your_tests),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )

                Row(modifier = Modifier.padding(top = 20.dp, bottom = 16.dp)) {
                    Text(
                        text = stringResource(R.string.mode_title),
                        modifier = Modifier.padding(end = 8.dp),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(mode, style = MaterialTheme.typography.bodyMedium)
                }

                Row {
                    Text(
                        text = stringResource(R.string.package_title),
                        modifier = Modifier.padding(end = 8.dp),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(packag, style = MaterialTheme.typography.bodyMedium)
                }

                Column(
                    Modifier
                        .fillMaxSize()
                        .weight(1f, false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 20.dp)
                    ) {

                        MyPrimaryButton(onClick = {
                            progressVisibity.value = true // show progress bar
                            viewModel?.askAttestation()
                        }, text = stringResource(R.string.request_an_attestation))

                        Box(modifier = Modifier.padding(vertical = 24.dp)) {
                            MyPrimaryButton(onClick = {
                                // Show PayDialogConfig()
                                showDialog.value = true
                            }, text = stringResource(R.string.make_a_transaction))
                        }

                        /*MyPrimaryButton(onClick = {
                            // Show PayDialogConfig()
                            showDialogRefund.value = true
                        }, text = stringResource(R.string.request_a_refund))*/
                    }

                    if (progressVisibity.value) {
                        CircularProgressIndicator(
                            color = LightGreen,
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                        )
                    }

                    if (showDialog.value) {
                        PayDialogConfig(setShowDialog = { showDialog.value = it })
                        { amount, tip, transitionAuto, externalTransactionReference ->

                            var finalAmount = amount
                            if (tip != null) finalAmount += tip
                            val intent = Intent().apply {
                                setClassName(
                                    famocopayPackageInstalled,
                                    Constants.CLASS_NAME
                                )
                                putExtra("amount", finalAmount)
                                if (externalTransactionReference!=null) putExtra(
                                    "externalTransactionReference",
                                    externalTransactionReference
                                )
                                putExtra("transitionAuto", transitionAuto)
                                if (tip != null) putExtra("tip", tip)
                            }

                            // Use the launcher to start an activity
                            myLauncher.launch(intent)

                        }
                    }
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
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel?.checkContactLessInstalled(packagesList)
            }
            Lifecycle.Event.ON_CREATE -> {

                viewModel?.resultAttestation?.observe(lifecycleOwner) { result ->
                    if (result != null) {
                        progressVisibity.value = false
                        onNextResult?.let { it(result, null) }
                    }
                }

                // Error attestation no response
                viewModel?.errorAttestation?.observe(lifecycleOwner) {
                    if (it != null) {
                        val result = ResultDTO(
                            false,
                            context.getString(R.string.response_delay_error),
                            context.getString(R.string.response_delay_error_title),
                            context.getString(R.string.response_delay_error_desc)
                        )
                        onNextResult?.let { it(result, null) }
                    }
                }

            }

            else -> null
        }
    }

    data class Params(
        val onNextResult: (ResultDTO?, TransactionDTO?) -> Unit,
        val onBackHome: () -> Unit
    )
}

