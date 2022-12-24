package dev.wxlf.cftbinlist.presentation.common

import dev.wxlf.cftbinlist.data.entities.RequestEntity

sealed class MainScreenViewState {
    object LoadingHistory : MainScreenViewState()
    data class LoadedHistory(val history: List<RequestEntity>) : MainScreenViewState()
}
