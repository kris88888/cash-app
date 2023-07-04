package com.example.cashappstocks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cashappstocks.network.StockService
import com.example.cashappstocks.network.data.Result
import com.example.cashappstocks.network.data.Stock
import com.example.cashappstocks.network.data.StockRetrofitInstance
import com.example.cashappstocks.network.data.StocksRepository
import com.example.cashappstocks.network.data.StocksRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val stockRepo: StocksRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()
    private val _refreshFlag = MutableStateFlow(false)
    val refreshFlag = _refreshFlag.asStateFlow()

    fun getData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                stockRepo.getStocks()
                    .catch {
                        Log.d("KRIS", "getData: ${it.message}")
                        _uiState.value = UIState(isError = true)
                    }.collect {
                        when (it) {
                            is Result.Loading -> _uiState.value = UIState(isLoading = true)
                            is Result.Success -> _uiState.value = UIState(stocks = it.data)
                            is Result.Error -> _uiState.value = UIState(isError = true)
                        }
                    }
            }
        }
    }

    data class UIState(
        val isLoading: Boolean = false,
        val stocks: List<Stock> = listOf(),
        val isError: Boolean? = null
    )
}


object CommonViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                StocksRepositoryImpl(
                    StockRetrofitInstance
                        .getRetrofitInstance()
                        .create(StockService::class.java)
                )
            ) as T
        } else {
            return super.create(modelClass)

        }
    }
}