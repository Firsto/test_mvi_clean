package studio.inprogress.simpleinvoices.ui.invoices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import studio.inprogress.simpleinvoices.databinding.FragmentInvoicesListBinding
import studio.inprogress.simpleinvoices.databinding.ItemInvoiceBinding
import studio.inprogress.simpleinvoices.ui.base.BaseMVIFragment
import studio.inprogress.simpleinvoices.ui.invoices.adapter.InvoiceItem
import studio.inprogress.simpleinvoices.util.Debug.debug
import studio.inprogress.simpleinvoices.util.toast

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class InvoicesListFragment :
    BaseMVIFragment<FragmentInvoicesListBinding, InvoicesViewModel, InvoicesContract.ViewState,
            InvoicesContract.SingleEvent, InvoicesContract.ViewIntent>() {

    override val viewModel: InvoicesViewModel by viewModels()
    override lateinit var binding: FragmentInvoicesListBinding

    private lateinit var adapter: ItemAdapter<InvoiceItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (findNavController() as? NavHostController)?.enableOnBackPressed(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInvoicesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupViews() {
        adapter = ItemAdapter()
        val fastAdapter = FastAdapter.with(adapter)
        fastAdapter.addClickListener<ItemInvoiceBinding, InvoiceItem>({binding->binding.root}) {
            v, position, adapter, item ->

            debug {
                "Clicked ${item.invoice.id} on position $position"
            }
        }

        binding.invoices.adapter = fastAdapter
    }

    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.processIntent(InvoicesContract.ViewIntent.Init)
        }
    }

    override fun render(viewState: InvoicesContract.ViewState) {
        debug { "ViewState changed: $viewState" }

        binding.progress.isVisible = viewState.isLoading
        if (viewState.isError) {
            binding.invoices.isVisible = false

            requireActivity().toast("Something wrong!")
        } else {
            binding.invoices.isVisible = true

            val items = viewState.data.asSequence().map { InvoiceItem(it) }.toList()
            adapter.set(items)
        }
    }

    override fun handleSingleEvent(event: InvoicesContract.SingleEvent) {
        when (event) {
            is InvoicesContract.SingleEvent.Failure -> {

            }
            is InvoicesContract.SingleEvent.OpenInvoice -> {

            }
            InvoicesContract.SingleEvent.Success -> {

            }
        }
    }

    override fun intents(): Flow<InvoicesContract.ViewIntent> = binding.run {
        merge(emptyFlow<InvoicesContract.ViewIntent>())
            .onStart {
                InvoicesContract.ViewIntent.Init
            }
    }
}
