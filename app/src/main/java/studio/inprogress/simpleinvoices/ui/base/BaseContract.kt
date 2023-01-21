package studio.inprogress.simpleinvoices.ui.base

import androidx.annotation.StringRes

object BaseContract {
    data class ViewState(
        val isShowModalLoading: Boolean,
        @StringRes
        val modalLoadingText: Int,
    ) {
        companion object {
            fun initial() = ViewState(
                isShowModalLoading = false,
                modalLoadingText = 0
            )
        }
    }

    sealed class ViewIntent {
        object Nothing : ViewIntent()
        object Init : ViewIntent()
    }

    sealed class PartialStateChange : BasePartialStateChange<ViewState>() {
        abstract override fun reduce(viewState: ViewState): ViewState
    }

    sealed class SingleEvent {
        object Nothing : SingleEvent()
    }
}
