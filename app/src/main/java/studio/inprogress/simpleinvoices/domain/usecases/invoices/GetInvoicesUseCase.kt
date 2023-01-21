package studio.inprogress.simpleinvoices.domain.usecases.invoices

import studio.inprogress.simpleinvoices.domain.IInvoicesRepository
import studio.inprogress.simpleinvoices.domain.Result
import studio.inprogress.simpleinvoices.domain.entity.Invoice
import studio.inprogress.simpleinvoices.domain.usecases.UseCase
import studio.inprogress.simpleinvoices.domain.usecases.invoices.api.IGetInvoicesUseCase
import javax.inject.Inject

class GetInvoicesUseCase @Inject constructor(private val repo: IInvoicesRepository) :
    IGetInvoicesUseCase {
    override suspend fun invoke(param: UseCase.Empty): Result<List<Invoice>> = repo.getInvoices()
}
