package com.groupec.githubfetchercompose.presentation.components.operation

import androidx.lifecycle.*
import com.groupec.githubfetchercompose.di.DispatcherDefault
import com.groupec.githubfetchercompose.domain.usecases.init.CheckInstallTaskUseCase
import com.groupec.githubfetchercompose.domain.usecases.transaction.SavePaymentTaskUseCase
import com.groupec.githubfetchercompose.domain.usecases.transaction.*
import com.groupec.githubfetchercompose.utils.Failure
import com.groupec.githubfetchercompose.utils.right
import com.groupec.famocopayapp2apptoolscompose.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class OperationViewModel @Inject constructor(
    private val attestationTaskUseCase: AttestationTaskUseCase,
    private val getConfigTaskUseCase: GetConfigTaskUseCase,
    private val getConfigStateUseCase: GetConfigStateUseCase,
    private val savePaymentTaskUseCase: SavePaymentTaskUseCase,
    private val getPaymentTaskUseCase: GetPaymentTaskUseCase,
    private val getFamocopayPackageTaskUseCase: GetFamocopayPackageTaskUseCase,
    private val checkInstallTaskUseCase: CheckInstallTaskUseCase,
    @DispatcherDefault defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    val settings =
        Transformations.map(getConfigStateUseCase.observe(viewModelScope.coroutineContext + defaultDispatcher)) { it?.right() }

    private val _error = MutableLiveData<Failure?>(null)
    val error: LiveData<Failure?> = _error

    private val _errorAttestation = SingleLiveEvent<Failure?>(null)
    var errorAttestation: MutableLiveData<Failure?> = _errorAttestation

    private val _savedResultAttestation = SingleLiveEvent<ResultDTO?>(null)
    var resultAttestation: MutableLiveData<ResultDTO?> = _savedResultAttestation

    private val _savedPaymentLocal = SingleLiveEvent<Boolean?>(null)
    var savedPaymentLocal: MutableLiveData<Boolean?> = _savedPaymentLocal

    private val _checkFamocopayPackage = SingleLiveEvent<Pair<Boolean, String>?>(null)
    var checkFamocopayPackage: MutableLiveData<Pair<Boolean, String>?> = _checkFamocopayPackage

    private val _checked = SingleLiveEvent<Boolean?>(null)
    var checked: MutableLiveData<Boolean?> = _checked

    val paymentList = MutableLiveData<List<PaymentDTO>>()


    fun askAttestation() {
        attestationTaskUseCase.execute(viewModelScope, Unit) {
            it.apply(::handleAttestationFailure, ::handleAttestationRetrieved)
        }
    }

    fun getConfig() {
        getConfigTaskUseCase.execute(viewModelScope, Unit) {
            it.apply(::handleFailure, ::nothingToDo)
        }
    }

    fun savePayment(transactionId: Int, fullReceipt: String, dateTransaction : String) {
        savePaymentTaskUseCase.execute(viewModelScope, PaymentDTO(transactionId, fullReceipt, dateTransaction)){
            it.apply(::handleFailure, ::handlePaymentRetrieved)
        }
    }

    fun checkFamocopayPackage() {
        getFamocopayPackageTaskUseCase.execute(viewModelScope, Unit) {
            it.apply(::handleFailure, ::handleCheckFamocopayPackage)
        }
    }

    fun getLocalPayment() {
        getPaymentTaskUseCase.execute(viewModelScope, Unit){
            it.apply(::handleFailure, ::handleGetPaymentRetrieved)
        }
    }

    fun checkContactLessInstalled(uri: List<String>) {
        checkInstallTaskUseCase.execute(viewModelScope, uri) {
            it.apply(::handleFailure, ::handleCheckFinished)
        }
    }

    private fun handleAttestationRetrieved(retrieved: ResultDTO) {
        resultAttestation.postValue(retrieved)
    }

    private fun handlePaymentRetrieved(retrieved: Boolean) {
        savedPaymentLocal.postValue(retrieved)
    }

    private fun handleGetPaymentRetrieved(retrieved: List<PaymentDTO>) {
        paymentList.postValue(retrieved)
    }

    private fun handleCheckFamocopayPackage(retrieved: Pair<Boolean, String>) {
        checkFamocopayPackage.postValue(retrieved)
    }

    private fun handleCheckFinished(value: Boolean) {
        checked.postValue(value)
    }

    private fun handleFailure(failure: Failure?) {
        _error.postValue(failure)
    }

    private fun handleAttestationFailure(failure: Failure?) {
        errorAttestation.postValue(failure)
    }

    private fun nothingToDo(retrieved: Boolean) {
        // No thing to do
    }
}