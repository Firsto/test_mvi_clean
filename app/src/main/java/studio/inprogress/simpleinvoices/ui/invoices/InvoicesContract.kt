package studio.inprogress.simpleinvoices.ui.invoices

import studio.inprogress.simpleinvoices.domain.entity.Invoice
import studio.inprogress.simpleinvoices.ui.base.BasePartialStateChange

object InvoicesContract {
    data class ViewState(
        val data: List<Invoice>,
        val isLoading: Boolean,
        val isError: Boolean,
        val selectedInvoice: Invoice? = null
    ) {
        companion object {
            fun initial() = ViewState(
                data = emptyList(),
                isLoading = false,
                isError = false,
                selectedInvoice = null
            )
        }
    }

    sealed class ViewIntent {
        data class OpenInvoice(val invoice: Invoice) : ViewIntent()
        object Nothing : ViewIntent()
        object Init : ViewIntent()
        object Empty : ViewIntent()
    }

    sealed class PartialStateChange : BasePartialStateChange<ViewState>() {

        object Nothing : PartialStateChange() {
            override fun reduce(viewState: ViewState): ViewState {
                return viewState
            }
        }

        data class OpenInvoice(val invoice: Invoice) : PartialStateChange() {
            override fun reduce(viewState: ViewState): ViewState {
                return viewState.copy(selectedInvoice = invoice)
            }
        }

        sealed class LoadInvoices : PartialStateChange() {
            object Loading : LoadInvoices()
            data class LoadSuccess(val data: List<Invoice>) : LoadInvoices()
            data class LoadFailure(val throwable: Throwable) :
                LoadInvoices()

            override fun reduce(viewState: ViewState): ViewState {
                return when (this) {
                    Loading -> viewState.copy(isLoading = true, isError = false)
                    is LoadSuccess -> viewState.copy(data = data, isLoading = false, isError = false)
                    is LoadFailure -> viewState.copy(isLoading = false, isError = true)
                }
            }
        }
    }

    sealed class SingleEvent {
        object Success : SingleEvent()
        data class OpenInvoice(val invoice: Invoice) : SingleEvent()
        data class Failure(val throwable: Throwable) : SingleEvent()
    }
}
