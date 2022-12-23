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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import dev.wxlf.cftbinlist.R
import dev.wxlf.cftbinlist.data.entities.BINInfoEntity
import dev.wxlf.cftbinlist.data.entities.BankEntity
import dev.wxlf.cftbinlist.data.entities.CountryEntity
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
    val focusManager = LocalFocusManager.current

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
                focusManager.clearFocus()
                if (bin.value.isNotEmpty()) {
                    viewModel.obtainEvent(MainScreenEvent.LoadBINInfo(bin.value))
                    bottomSheetScope.launch {
                        if (!binInfoDialog.isVisible) {
                            binInfoDialog.show()
                        }
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
                        CardInfo(binInfo = binInfo.value)
                        if (binInfo.value.bank!! != BankEntity())
                            BankInfo(bankInfo = binInfo.value.bank!!)
                        if (binInfo.value.country != null)
                            CountryInfo(countryInfo = binInfo.value.country!!)
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

@Composable
fun CardInfo(binInfo: BINInfoEntity) {
    FlowRow(
        mainAxisSpacing = 16.dp,
        crossAxisSpacing = 8.dp
    ) {
        Column {
            Text("Платёжная система", fontSize = 12.sp)
            if (binInfo.scheme.isNullOrEmpty())
                Text("--", fontWeight = FontWeight.Bold)
            else
                Text(binInfo.scheme!!.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }, fontWeight = FontWeight.Bold)
        }

        Column {
            Text("Тип карты", fontSize = 12.sp)
            if (binInfo.type.isNullOrEmpty())
                Text("--", fontWeight = FontWeight.Bold)
            else
                Text(binInfo.type!!.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }, fontWeight = FontWeight.Bold)
        }

        Column {
            Text("Бренд", fontSize = 12.sp)
            if (binInfo.brand.isNullOrEmpty())
                Text("--", fontWeight = FontWeight.Bold)
            else
                Text(binInfo.brand!!.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }, fontWeight = FontWeight.Bold)
        }

        Column {
            Text("Предоплата", fontSize = 12.sp)
            if (binInfo.prepaid == true)
                Text("Да", fontWeight = FontWeight.Bold)
            else
                Text("Нет", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CountryInfo(countryInfo: CountryEntity) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Страна", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        if (!countryInfo.name.isNullOrEmpty() && !countryInfo.emoji.isNullOrEmpty())
            Text(
                "${countryInfo.emoji!!} ${countryInfo.name!!}",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        if (!countryInfo.currency.isNullOrEmpty())
            Text("Валюта: ${countryInfo.currency}", modifier = Modifier.padding(vertical = 8.dp))
        if (countryInfo.latitude != null && countryInfo.longitude != null)
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        uriHandler.openUri("geo:${countryInfo.latitude},${countryInfo.longitude}")
                    }
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("${countryInfo.latitude}, ${countryInfo.longitude}")
            }
    }
}

@Composable
fun BankInfo(bankInfo: BankEntity) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Банк", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        if (!bankInfo.name.isNullOrEmpty())
            Text(bankInfo.name!!, modifier = Modifier.padding(vertical = 8.dp))
        if (!bankInfo.url.isNullOrEmpty())
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        uriHandler.openUri("https://${bankInfo.url!!}")
                    }
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_link_24),
                    contentDescription = "Link",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    bankInfo.url!!,
                    color = Color(0xFF3F72AF),
                    textDecoration = TextDecoration.Underline,
                )
            }
        if (!bankInfo.phone.isNullOrEmpty())
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        uriHandler.openUri("tel:${bankInfo.phone!!}")
                    }
            ) {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = "Phone",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(bankInfo.phone!!)
            }

        if (!bankInfo.city.isNullOrEmpty())
            Text(bankInfo.city!!, modifier = Modifier.padding(vertical = 8.dp))
    }
}