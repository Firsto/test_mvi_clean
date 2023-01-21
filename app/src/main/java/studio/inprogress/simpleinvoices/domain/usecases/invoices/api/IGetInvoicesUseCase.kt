package studio.inprogress.simpleinvoices.domain.usecases.invoices.api

import studio.inprogress.simpleinvoices.domain.entity.Invoice
import studio.inprogress.simpleinvoices.domain.Result
import studio.inprogress.simpleinvoices.domain.usecases.UseCase

interface IGetInvoicesUseCase : UseCase<Result<List<Invoice>>, UseCase.Empty> {
    override suspend fun invoke(param: UseCase.Empty): Result<List<Invoice>>
}
