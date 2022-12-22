package dev.wxlf.cftbinlist.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.wxlf.cftbinlist.R
import dev.wxlf.cftbinlist.data.entities.BINInfoEntity
import dev.wxlf.cftbinlist.presentation.common.MainScreenEvent
import dev.wxlf.cftbinlist.presentation.common.MainScreenViewState
import dev.wxlf.cftbinlist.presentation.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun MainScreen(viewModel: MainViewModel, colorScheme: ColorScheme) {
    val uiState by viewModel.uiState.collectAsState()
    val bin = remember { mutableStateOf("") }
    val binInfoDialog = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val binInfo = remember { mutableStateOf(BINInfoEntity()) }
    val bottomSheetScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    when (uiState) {
        is MainScreenViewState.Loaded -> {
            binInfo.value = (uiState as MainScreenViewState.Loaded).data
        }
        MainScreenViewState.Loading -> {}
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier
                .padding(horizontal = 56.dp, vertical = 16.dp)
                .height(56.dp),
            value = bin.value,
            onValueChange = { input ->
                bin.value = input
                bottomSheetScope.launch {
                    if (!binInfoDialog.isVisible) {
                        binInfoDialog.show()
                    }
                }
            },
            label = { Text(text = "BIN") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_outline_cancel_24),
                    contentDescription = "Clear",
                    modifier = Modifier.clickable {
                        bin.value = ""
                    }
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
                bottomSheetScope.launch {
                    if (!binInfoDialog.isVisible) {
                        binInfoDialog.show()
                    }
                }
            })
        )
        Divider(
            modifier = Modifier.padding(horizontal = 26.dp)
        )
    }

    ModalBottomSheetLayout(
        sheetContent = {
            Text(binInfo.value.toString())
        },
        sheetState = binInfoDialog,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetBackgroundColor = colorScheme.background
    ) {

    }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.obtainEvent(MainScreenEvent.ScreenShown)
    })
}