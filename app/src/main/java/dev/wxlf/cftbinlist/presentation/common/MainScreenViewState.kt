package dev.wxlf.cftbinlist.presentation.common

import dev.wxlf.cftbinlist.data.entities.BINInfoEntity

sealed class MainScreenViewState {
    object Loading : MainScreenViewState()
    data class Loaded(val data: BINInfoEntity) : MainScreenViewState()
}
