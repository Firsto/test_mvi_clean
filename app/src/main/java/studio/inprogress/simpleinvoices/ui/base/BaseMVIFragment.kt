package studio.inprogress.simpleinvoices.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
abstract class BaseMVIFragment<
        Binding : ViewBinding,
        FragmentViewModel : BaseViewModel<State, SingleEvent, Intent>,
        State, SingleEvent, Intent,
        > : Fragment() {

    protected abstract var binding: Binding
    protected abstract val viewModel: FragmentViewModel

    abstract fun intents(): Flow<Intent>
    abstract fun render(viewState: State)
    abstract fun handleSingleEvent(event: SingleEvent)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        bindVM()
    }

    protected open fun setupViews() {}

    protected open fun bindVM() {
        intents().onEach { viewModel.processIntent(it) }.launchIn(lifecycleScope)
        viewModel.viewState.onEach { render(it) }.launchIn(lifecycleScope)
//        lifecycleScope.launchWhenStarted { viewModel.viewState.collect { render(it) } }
        lifecycleScope.launchWhenStarted { viewModel.singleEvent.collect { handleSingleEvent(it) } }
    }
}
