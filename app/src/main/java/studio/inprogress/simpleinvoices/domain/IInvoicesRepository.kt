package studio.inprogress.simpleinvoices.domain

import studio.inprogress.simpleinvoices.domain.entity.Invoice

interface IInvoicesRepository {
    suspend fun getInvoices() : Result<List<Invoice>>
}
