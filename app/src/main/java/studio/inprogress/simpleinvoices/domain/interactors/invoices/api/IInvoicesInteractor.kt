package studio.inprogress.simpleinvoices.domain.interactors.invoices.api

import studio.inprogress.simpleinvoices.domain.entity.Invoice
import studio.inprogress.simpleinvoices.domain.Result

interface IInvoicesInteractor {
    suspend fun getInvoices(): Result<List<Invoice>>
}
