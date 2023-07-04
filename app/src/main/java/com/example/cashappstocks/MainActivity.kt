package com.example.cashappstocks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cashappstocks.network.StockService
import com.example.cashappstocks.network.data.StockRetrofitInstance
import com.example.cashappstocks.network.data.StocksRepositoryImpl
import com.example.cashappstocks.ui.theme.CashAppStocksTheme

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels {
        CommonViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CashAppStocksTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state = viewModel.uiState.collectAsState()
                    val uiState = remember {
                        state
                    }
                    LaunchedEffect(key1 = true) {
                        viewModel.getData()
                    }
                    if (uiState.value.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        if (uiState.value.isError == true) {
                            Greeting(name = "ERROR")
                        } else {
                            Greeting(
                                "${
                                    uiState
                                        .value
                                        .stocks
                                    
                                }"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CashAppStocksTheme {
        Greeting("Android")
    }
}