package studio.inprogress.simpleinvoices.domain.interactors.invoices

import studio.inprogress.simpleinvoices.domain.Result
import studio.inprogress.simpleinvoices.domain.entity.Invoice
import studio.inprogress.simpleinvoices.domain.interactors.invoices.api.IInvoicesInteractor
import studio.inprogress.simpleinvoices.domain.usecases.invoices.api.IGetInvoicesUseCase
import javax.inject.Inject

class InvoicesInteractor @Inject constructor(private val getInvoicesUseCase: IGetInvoicesUseCase) :
    IInvoicesInteractor {
    override suspend fun getInvoices(): Result<List<Invoice>> = getInvoicesUseCase()
}
