package dev.wxlf.cftbinlist.presentation.common

import dev.wxlf.cftbinlist.data.entities.BINInfoEntity

sealed class BinInfoViewState {
    object LoadingBINInfo : BinInfoViewState()
    data class LoadedBINInfo(val data: BINInfoEntity) : BinInfoViewState()
}
