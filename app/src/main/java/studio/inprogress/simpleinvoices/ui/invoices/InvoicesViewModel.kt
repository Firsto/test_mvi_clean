package studio.inprogress.simpleinvoices.ui.invoices

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import studio.inprogress.simpleinvoices.domain.Result
import studio.inprogress.simpleinvoices.domain.interactors.invoices.api.IInvoicesInteractor
import studio.inprogress.simpleinvoices.ui.base.BasePartialStateChange
import studio.inprogress.simpleinvoices.ui.base.BaseViewModel
import studio.inprogress.simpleinvoices.ui.invoices.InvoicesContract.PartialStateChange
import studio.inprogress.simpleinvoices.ui.invoices.InvoicesContract.SingleEvent
import studio.inprogress.simpleinvoices.ui.invoices.InvoicesContract.ViewIntent
import studio.inprogress.simpleinvoices.ui.invoices.InvoicesContract.ViewState
import studio.inprogress.simpleinvoices.util.Debug.debug
import studio.inprogress.simpleinvoices.util.flatMapFirst
import javax.inject.Inject

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class InvoicesViewModel @Inject constructor(private val interactor: IInvoicesInteractor) :
    BaseViewModel<ViewState, SingleEvent, ViewIntent>() {
    override val initialViewState: ViewState
        get() = ViewState.initial()

    override fun Flow<BasePartialStateChange<ViewState>>.sendSingleEvent(): Flow<BasePartialStateChange<ViewState>> {
        return onEach { change ->
            val event = when (change) {
                is PartialStateChange.OpenInvoice -> {
                    SingleEvent.OpenInvoice(change.invoice)
                }
                is PartialStateChange.LoadInvoices.LoadFailure -> {
                    SingleEvent.Failure(change.throwable)
                }
                is PartialStateChange.LoadInvoices.LoadSuccess -> {
                    SingleEvent.Success
                }
                is PartialStateChange.LoadInvoices.Loading -> {
                    // no op
                    return@onEach
                }
                else -> return@onEach
            }

            _singleEvent.trySend(event)
        }
    }

    override fun Flow<ViewIntent>.toPartialStateChangesFlow(): Flow<PartialStateChange> {
        val init = filterIsInstance<ViewIntent.Init>()
            .flatMapFirst {
                launchIn(viewModelScope)
                flow {
                    debug { "init invoices loading" }
                    emit(interactor.getInvoices())
                }
                    .map {
                        when (it) {
                            is Result.Success -> {
                                PartialStateChange.LoadInvoices.LoadSuccess(it.data)
                            }
                            is Result.Error -> {
                                PartialStateChange.LoadInvoices.LoadFailure(it.exception ?: Throwable(it.message))
                            }
                        }
                    }
                    .onStart {
                        emit(PartialStateChange.LoadInvoices.Loading)
                    }
                    .catch {
                        emit(PartialStateChange.LoadInvoices.LoadFailure(Throwable("Failed to load invoices!")))
                    }
            }

        val openInvoice = filterIsInstance<ViewIntent.OpenInvoice>()
            .map { PartialStateChange.OpenInvoice(it.invoice) }

        return merge(init, openInvoice)
    }
}
