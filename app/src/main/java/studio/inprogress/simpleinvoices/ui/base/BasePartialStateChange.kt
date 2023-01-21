package studio.inprogress.simpleinvoices.ui.base

abstract class BasePartialStateChange<ViewState> {

    abstract fun reduce(viewState: ViewState): ViewState

}
