package studio.inprogress.simpleinvoices.api

import retrofit2.Response
import retrofit2.http.GET
import studio.inprogress.simpleinvoices.api.response.InvoicesResponse

interface InvoiceApiService {
    @GET("xmm-homework/invoices.json")
    suspend fun getInvoices(): Response<InvoicesResponse>

    @GET("xmm-homework/invoices_malformed.json")
    suspend fun getInvoicesMalformed(): Response<InvoicesResponse>

    @GET("xmm-homework/invoices_empty.json")
    suspend fun getInvoicesEmpty(): Response<InvoicesResponse>
}
