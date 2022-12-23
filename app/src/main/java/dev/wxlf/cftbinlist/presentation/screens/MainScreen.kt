package dev.wxlf.cftbinlist.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.wxlf.cftbinlist.R
import dev.wxlf.cftbinlist.data.entities.BINInfoEntity
import dev.wxlf.cftbinlist.presentation.common.MainScreenEvent
import dev.wxlf.cftbinlist.presentation.common.MainScreenViewState
import dev.wxlf.cftbinlist.presentation.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import java.util.*

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
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
        is MainScreenViewState.LoadedBINInfo -> {
            binInfo.value = (uiState as MainScreenViewState.LoadedBINInfo).data
        }
        MainScreenViewState.LoadingBINInfo -> {}
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
//                viewModel.obtainEvent(MainScreenEvent.LoadBINInfo(bin.value))
//                bottomSheetScope.launch {
//                    if (!binInfoDialog.isVisible) {
//                        binInfoDialog.show()
//                    }
//                }
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
                viewModel.obtainEvent(MainScreenEvent.LoadBINInfo(bin.value))
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_drag_handle),
                    contentDescription = "Drag handle",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                )
                when (uiState) {
                    is MainScreenViewState.LoadedBINInfo -> {
                        Text(
                            bin.value,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 0.dp, bottom = 16.dp),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row() {
                            Column() {
                                Text("Платёжная система", fontSize = 12.sp)
                                if (binInfo.value.scheme.isNullOrEmpty())
                                    Text("--", fontWeight = FontWeight.Bold)
                                else
                                    Text(binInfo.value.scheme!!.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.getDefault()
                                        ) else it.toString()
                                    }, fontWeight = FontWeight.Bold)
                            }

                            Column(
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                Text("Тип карты", fontSize = 12.sp)
                                if (binInfo.value.type.isNullOrEmpty())
                                    Text("--", fontWeight = FontWeight.Bold)
                                else
                                    Text(binInfo.value.type!!.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.getDefault()
                                        ) else it.toString()
                                    }, fontWeight = FontWeight.Bold)
                            }

                            Column(
                                modifier = Modifier.padding(end = 16.dp)
                            ) {
                                Text("Бренд", fontSize = 12.sp)
                                if (binInfo.value.brand.isNullOrEmpty())
                                    Text("--", fontWeight = FontWeight.Bold)
                                else
                                    Text(binInfo.value.brand!!.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.getDefault()
                                        ) else it.toString()
                                    }, fontWeight = FontWeight.Bold)
                            }

                            Column() {
                                Text("Предоплата", fontSize = 12.sp)
                                if (binInfo.value.prepaid == true)
                                    Text("Да", fontWeight = FontWeight.Bold)
                                else
                                    Text("Нет", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    MainScreenViewState.LoadingBINInfo -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        )
                    }
                }
            }
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