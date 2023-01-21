package studio.inprogress.simpleinvoices.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import studio.inprogress.simpleinvoices.api.InvoiceApiService
import studio.inprogress.simpleinvoices.data.source.InvoicesRepository
import studio.inprogress.simpleinvoices.domain.IInvoicesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideInvoicesRepository(api: InvoiceApiService): IInvoicesRepository =
        InvoicesRepository(api)
}
