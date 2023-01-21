package studio.inprogress.simpleinvoices.data.source

import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Response
import studio.inprogress.simpleinvoices.api.InvoiceApiService
import studio.inprogress.simpleinvoices.domain.IInvoicesRepository
import studio.inprogress.simpleinvoices.domain.Result
import studio.inprogress.simpleinvoices.domain.entity.Invoice
import timber.log.Timber
import javax.inject.Inject

class InvoicesRepository @Inject constructor(
    private val api: InvoiceApiService
) : IInvoicesRepository {
    private val supervisorJob = SupervisorJob()
    private val repositoryScope = CoroutineScope(Dispatchers.Main + supervisorJob)

    override suspend fun getInvoices(): Result<List<Invoice>> =
        withContext(repositoryScope.coroutineContext + Dispatchers.IO) {
            try {
                val response = api.getInvoices()

                if (!response.isSuccessful) {
                    val error = handleError(response)

                    Timber.d("Load invoices error: ${error.message}")

                    return@withContext error
                }

                response.body()?.let {
                    return@withContext Result.Success(it.items)
                } ?: return@withContext Result.Success(emptyList())
            } catch (e: Exception) {
                return@withContext Result.Error("Failed to load invoices.")
            }
        }

    private fun <T> handleError(response: Response<T>): Result.Error {
        val errorBody = response.errorBody()?.string()

        return try {
            val json = JSONObject(errorBody!!)
            val code = json.getInt("code")
            Result.Error(httpCode = response.code(), errorCode = code, errorJson = json)
        } catch (e: Exception) {
            Result.Error(message = errorBody)
        }
    }
}
