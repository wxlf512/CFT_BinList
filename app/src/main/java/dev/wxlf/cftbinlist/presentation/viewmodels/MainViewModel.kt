package dev.wxlf.cftbinlist.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.wxlf.cftbinlist.domain.usecases.AddRequestToHistoryUseCase
import dev.wxlf.cftbinlist.domain.usecases.DeleteRequestFromHistoryUseCase
import dev.wxlf.cftbinlist.domain.usecases.FetchBINInfoUseCase
import dev.wxlf.cftbinlist.domain.usecases.FetchRequestsHistoryUseCase
import dev.wxlf.cftbinlist.presentation.common.BinInfoViewState
import dev.wxlf.cftbinlist.presentation.common.MainScreenEvent
import dev.wxlf.cftbinlist.presentation.common.MainScreenViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchBINInfoUseCase: FetchBINInfoUseCase,
    private val fetchRequestsHistoryUseCase: FetchRequestsHistoryUseCase,
    private val addRequestToHistoryUseCase: AddRequestToHistoryUseCase,
    private val deleteRequestFromHistoryUseCase: DeleteRequestFromHistoryUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<MainScreenViewState>(MainScreenViewState.InitialScreen)
    val uiState: StateFlow<MainScreenViewState> = _uiState

    private val _binInfoState =
        MutableStateFlow<BinInfoViewState>(BinInfoViewState.InitialState)
    val binInfoState: StateFlow<BinInfoViewState> = _binInfoState

    fun obtainEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.LoadHistory -> {
                viewModelScope.launch {
                    val history = fetchRequestsHistoryUseCase.execute()
                    _uiState.emit(MainScreenViewState.LoadedHistory(history))
                }
            }
            is MainScreenEvent.LoadBINInfo -> {
                val bin = event.bin.replace("[^0-9]".toRegex(), "")
                viewModelScope.launch {
                    try {
                        val data = fetchBINInfoUseCase.execute(bin)
                        _binInfoState.emit(BinInfoViewState.LoadedBINInfo(data))
                    } catch (e: Throwable) {
                        _binInfoState.emit(BinInfoViewState.ErrorState(msg = e.localizedMessage.orEmpty()))
                    }
                }
            }
            is MainScreenEvent.DeleteRequest -> {
                viewModelScope.launch {
                    deleteRequestFromHistoryUseCase.execute(event.requestEntity)
                    val history = fetchRequestsHistoryUseCase.execute()
                    _uiState.emit(MainScreenViewState.LoadedHistory(history))
                }
            }
            is MainScreenEvent.AddRequest -> {
                viewModelScope.launch {
                    addRequestToHistoryUseCase.execute(event.requestEntity)
                    val history = fetchRequestsHistoryUseCase.execute()
                    _uiState.emit(MainScreenViewState.LoadedHistory(history))
                }
            }
        }
    }
}