package studio.inprogress.simpleinvoices.domain.entity

import com.google.gson.annotations.SerializedName

data class Invoice(
    @SerializedName("id")
    val id: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("items")
    val items: List<InvoiceItem> = listOf()
) {
    data class InvoiceItem(
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("quantity")
        val quantity: Int,
        @SerializedName("priceInCents")
        val priceInCents: Int
    )
}
