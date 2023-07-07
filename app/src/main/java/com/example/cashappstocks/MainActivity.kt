package com.example.cashappstocks

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cashappstocks.screens.ErrorScreen
import com.example.cashappstocks.screens.MainScreen
import com.example.cashappstocks.screens.ProgressScreen
import com.example.cashappstocks.ui.theme.CashAppStocksTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        CommonViewModelFactory
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CashAppStocksTheme {
                // A surface container using the 'background' color from the theme
                val snackbarHostState = remember {
                    SnackbarHostState()
                }
                Scaffold(
                    topBar = {
                        Surface(shadowElevation = 2.dp) {
                            TopAppBar(title = {
                                Text(text = stringResource(id = R.string.app_bar_text))
                            }
                            )
                        }
                    },
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) { padding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp, end = 8.dp),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val uiState = viewModel.uiState.collectAsState()
                        var needsRefresh by remember {
                            mutableStateOf(true)
                        }
                        LaunchedEffect(key1 = needsRefresh) {
                            viewModel.getData()
                        }
                        if (uiState.value.isLoading) {
                            ProgressScreen()
                        } else {
                            if (uiState.value.isError == true) {
                                val errorMessage = when (uiState.value.errprCategory) {
                                    ErrorCategory.NoData -> stringResource(id = R.string.no_data_found)
                                    else -> stringResource(id = R.string.error_message)
                                }
                                ErrorScreen(
                                    snackbarHostState = snackbarHostState,
                                    errorMessage = errorMessage
                                ) {
                                    needsRefresh = !needsRefresh
                                }
                            } else {
                                MainScreen(
                                    stocks = uiState.value.stocks,
                                    modifier = Modifier.padding(padding),
                                    convertDateFn = viewModel::getFormattedDate
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}