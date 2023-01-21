package studio.inprogress.simpleinvoices.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import studio.inprogress.simpleinvoices.util.Debug.debug

abstract class BaseViewModel<ViewState, SingleEvent, ViewIntent> :
    ViewModel() {

    protected abstract val initialViewState: ViewState

    internal open val _singleEvent = Channel<SingleEvent>()
    val singleEvent: Flow<SingleEvent> get() = _singleEvent.receiveAsFlow()

    private val _intentFlow = MutableSharedFlow<ViewIntent>(extraBufferCapacity = 64)

    val viewState: StateFlow<ViewState> by lazy {
        _intentFlow
            .toPartialStateChangesFlow()
            .sendSingleEvent()
            .scan(initialViewState) { state, change -> change.reduce(state) }
            .catch { debug { "[${this::class.java.simpleName}] Throwable: $it" } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialViewState)
    }

    abstract fun Flow<BasePartialStateChange<ViewState>>.sendSingleEvent(): Flow<BasePartialStateChange<ViewState>>

    abstract fun Flow<ViewIntent>.toPartialStateChangesFlow(): Flow<BasePartialStateChange<ViewState>>

    suspend fun processIntent(intent: ViewIntent) = _intentFlow.emit(intent)

    protected fun resetIntentFlow() = _intentFlow.resetReplayCache()
}
