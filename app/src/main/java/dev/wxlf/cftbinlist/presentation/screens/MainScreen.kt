package dev.wxlf.cftbinlist.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import dev.wxlf.cftbinlist.data.entities.*
import dev.wxlf.cftbinlist.presentation.common.BinInfoViewState
import dev.wxlf.cftbinlist.presentation.common.MainScreenEvent
import dev.wxlf.cftbinlist.presentation.common.MainScreenViewState
import dev.wxlf.cftbinlist.presentation.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun MainScreen(viewModel: MainViewModel, colorScheme: ColorScheme) {
    val uiState by viewModel.uiState.collectAsState()
    val binInfoState by viewModel.binInfoState.collectAsState()
    val inputBin = remember { mutableStateOf("") }
    val bin = remember { mutableStateOf("") }
    val binInfoDialog = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val binInfo = remember { mutableStateOf(BINInfoEntity()) }
    val history = remember { mutableStateListOf<RequestEntity>() }
    val bottomSheetScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier
                .padding(start = 56.dp, end = 56.dp, top = 16.dp, bottom = 8.dp)
                .height(56.dp),
            value = inputBin.value,
            onValueChange = { input ->
                inputBin.value = input
            },
            label = { Text(text = "BIN") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_outline_cancel_24),
                    contentDescription = "Clear",
                    modifier = Modifier.clickable {
                        inputBin.value = ""
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
                if (inputBin.value.isNotEmpty()) {
                    bin.value = inputBin.value
                    viewModel.obtainEvent(MainScreenEvent.LoadBINInfo(inputBin.value))
                    bottomSheetScope.launch {
                        if (!binInfoDialog.isVisible) {
                            binInfoDialog.show()
                        }
                    }
                    viewModel.obtainEvent(
                        MainScreenEvent.AddRequest(
                            RequestEntity(
                                bin = inputBin.value,
                                timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
                            )
                        )
                    )
                }
            })
        )
        Text("История запросов", fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
        when (uiState) {
            is MainScreenViewState.LoadedHistory -> {
                history.clear()
                history.addAll((uiState as MainScreenViewState.LoadedHistory).history.reversed())
            }
            MainScreenViewState.InitialScreen -> {}
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(history.toList()) { requestEntity ->
                Divider(
                    modifier = Modifier.padding(horizontal = 26.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            bin.value = requestEntity.bin
                            viewModel.obtainEvent(MainScreenEvent.LoadBINInfo(requestEntity.bin))
                            bottomSheetScope.launch {
                                if (!binInfoDialog.isVisible) {
                                    binInfoDialog.show()
                                }
                            }
                        }
                        .padding(vertical = 16.dp, horizontal = 26.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        requestEntity.bin,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val timestamp = Instant.parse(requestEntity.timestamp)
                            .atZone(ZoneId.systemDefault())
                        Text(timestamp.format(DateTimeFormatter.ofPattern("HH:mm\ndd/MM/yyyy")))
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete request",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(32.dp)
                                .clickable {
                                    viewModel.obtainEvent(
                                        MainScreenEvent.DeleteRequest(
                                            requestEntity
                                        )
                                    )
                                }
                        )
                    }
                }
            }
        }
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
                when (binInfoState) {
                    is BinInfoViewState.LoadedBINInfo -> {
                        binInfo.value = (binInfoState as BinInfoViewState.LoadedBINInfo).data
                        Text(
                            bin.value,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 0.dp, bottom = 16.dp),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        CardInfo(binInfo = binInfo.value)
                        if (binInfo.value.number != null && binInfo.value.number != NumberEntity())
                            NumberInfo(numberInfo = binInfo.value.number!!)
                        if (binInfo.value.bank != null && binInfo.value.bank!! != BankEntity())
                            BankInfo(bankInfo = binInfo.value.bank!!)
                        if (binInfo.value.country != null && binInfo.value.country != CountryEntity())
                            CountryInfo(countryInfo = binInfo.value.country!!)
                    }
                    BinInfoViewState.InitialState -> {}
                    is BinInfoViewState.ErrorState -> {
                        Text(
                            (binInfoState as BinInfoViewState.ErrorState).msg,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.SemiBold
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
        viewModel.obtainEvent(MainScreenEvent.LoadHistory)
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
            Text(
                bankInfo.name!!,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        if (!bankInfo.url.isNullOrEmpty())
            Row(
                modifier = Modifier
                    .padding(vertical = 4.dp)
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
                    .padding(vertical = 4.dp)
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
            Text(bankInfo.city!!, modifier = Modifier.padding(vertical = 4.dp))
    }
}

@Composable
fun NumberInfo(numberInfo: NumberEntity) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Карта",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Column(modifier = Modifier.padding(end = 16.dp)) {
                Text("Длинна номера", fontSize = 12.sp)
                if (numberInfo.length == null)
                    Text("--", fontWeight = FontWeight.Bold)
                else
                    Text("${numberInfo.length}", fontWeight = FontWeight.Bold)
            }
            Column {
                Text("Алгоритм Луна", fontSize = 12.sp)
                if (numberInfo.luhn == null)
                    Text("--", fontWeight = FontWeight.Bold)
                else
                    Text(if (numberInfo.luhn!!) "Да" else "Нет", fontWeight = FontWeight.Bold)
            }
        }
    }
}