package studio.inprogress.simpleinvoices.api.response

import com.google.gson.annotations.SerializedName
import studio.inprogress.simpleinvoices.domain.entity.Invoice

data class InvoicesResponse(
    @SerializedName("items") val items: List<Invoice> = listOf()
)
