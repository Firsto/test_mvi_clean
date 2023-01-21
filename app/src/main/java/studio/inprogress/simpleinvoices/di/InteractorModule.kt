package studio.inprogress.simpleinvoices.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import studio.inprogress.simpleinvoices.domain.interactors.invoices.InvoicesInteractor
import studio.inprogress.simpleinvoices.domain.interactors.invoices.api.IInvoicesInteractor

@Module(includes = [UseCaseModule::class])
@InstallIn(ViewModelComponent::class)
abstract class InteractorModule {
    @ViewModelScoped
    @Binds
    internal abstract fun bindInvoicesInteractor(interactor: InvoicesInteractor): IInvoicesInteractor
}
