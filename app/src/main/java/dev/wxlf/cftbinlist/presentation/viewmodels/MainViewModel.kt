package dev.wxlf.cftbinlist.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.wxlf.cftbinlist.domain.usecases.FetchBINInfoUseCase
import dev.wxlf.cftbinlist.presentation.common.MainScreenEvent
import dev.wxlf.cftbinlist.presentation.common.MainScreenViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchBINInfoUseCase: FetchBINInfoUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<MainScreenViewState>(MainScreenViewState.LoadingBINInfo)
    val uiState: StateFlow<MainScreenViewState> = _uiState

    fun obtainEvent(event: MainScreenEvent) {
        when(event) {
            MainScreenEvent.ScreenShown -> {
                /* Requests history */
            }
            is MainScreenEvent.LoadBINInfo -> {
                _uiState.value = MainScreenViewState.LoadingBINInfo
                val bin = event.bin.replace("[^0-9]".toRegex(), "")
                viewModelScope.launch {
                    val data = fetchBINInfoUseCase.execute(bin)
                    _uiState.emit(MainScreenViewState.LoadedBINInfo(data))
                }
            }
        }
    }
}