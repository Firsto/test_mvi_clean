package studio.inprogress.simpleinvoices.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import studio.inprogress.simpleinvoices.domain.usecases.invoices.GetInvoicesUseCase
import studio.inprogress.simpleinvoices.domain.usecases.invoices.api.IGetInvoicesUseCase

@Module(
    includes = [
        UseCaseModule.InvoicesBindings::class
    ]
)
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Module
    @InstallIn(ViewModelComponent::class)
    internal interface InvoicesBindings {
        @Binds
        fun bindGetMemesUseCase(useCase: GetInvoicesUseCase): IGetInvoicesUseCase
    }
}
