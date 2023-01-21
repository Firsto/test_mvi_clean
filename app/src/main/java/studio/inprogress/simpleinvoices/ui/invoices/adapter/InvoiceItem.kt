package studio.inprogress.simpleinvoices.ui.invoices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import studio.inprogress.simpleinvoices.R
import studio.inprogress.simpleinvoices.databinding.ItemInvoiceBinding
import studio.inprogress.simpleinvoices.domain.entity.Invoice

class InvoiceItem(val invoice: Invoice) : AbstractBindingItem<ItemInvoiceBinding>() {

    override val type: Int
        get() = R.id.invoice_item

    override fun bindView(binding: ItemInvoiceBinding, payloads: List<Any>) {
        with(binding) {
            id.text = invoice.id
            date.text = invoice.date
            desription.text = invoice.description
            items.text = invoice.items.size.toString()
        }
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemInvoiceBinding =
        ItemInvoiceBinding.inflate(inflater, parent, false)
}
