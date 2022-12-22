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
        MutableStateFlow<MainScreenViewState>(MainScreenViewState.Loading)
    val uiState: StateFlow<MainScreenViewState> = _uiState

    fun obtainEvent(event: MainScreenEvent) {
        when(event) {
            MainScreenEvent.ScreenShown -> {
                _uiState.value = MainScreenViewState.Loading
                viewModelScope.launch {
                    val data = fetchBINInfoUseCase.execute("53051330")
                    _uiState.emit(MainScreenViewState.Loaded(data))
                }
            }
        }
    }
}