package dev.wxlf.cftbinlist.presentation.common

import dev.wxlf.cftbinlist.data.entities.BINInfoEntity

sealed class MainScreenViewState {
    object LoadingBINInfo : MainScreenViewState()
    data class LoadedBINInfo(val data: BINInfoEntity) : MainScreenViewState()
}
