package dev.wxlf.cftbinlist.presentation.common

import dev.wxlf.cftbinlist.data.entities.RequestEntity

sealed class MainScreenViewState {
    object InitialScreen : MainScreenViewState()
    data class LoadedHistory(val history: List<RequestEntity>) : MainScreenViewState()
}
