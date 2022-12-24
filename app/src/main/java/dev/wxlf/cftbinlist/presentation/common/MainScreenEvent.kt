package dev.wxlf.cftbinlist.presentation.common

import dev.wxlf.cftbinlist.data.entities.RequestEntity

sealed class MainScreenEvent {
    object LoadHistory : MainScreenEvent()
    data class LoadBINInfo(val bin: String) : MainScreenEvent()
    data class AddRequest(val requestEntity: RequestEntity) : MainScreenEvent()
    data class DeleteRequest(val requestEntity: RequestEntity) : MainScreenEvent()
}
