package dev.wxlf.cftbinlist.presentation.common

sealed class MainScreenEvent {
    object ScreenShown : MainScreenEvent()
    data class LoadBINInfo(var bin: String) : MainScreenEvent()
}
