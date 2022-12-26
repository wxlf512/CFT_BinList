package dev.wxlf.cftbinlist.presentation.common

import dev.wxlf.cftbinlist.data.entities.BINInfoEntity

sealed class BinInfoViewState {
    object InitialState : BinInfoViewState()
    data class LoadedBINInfo(val data: BINInfoEntity) : BinInfoViewState()
    data class ErrorState(val msg: String) : BinInfoViewState()
}
