package com.example.cashappstocks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cashappstocks.network.StockService
import com.example.cashappstocks.network.data.Result
import com.example.cashappstocks.network.data.Stock
import com.example.cashappstocks.network.StockRetrofitInstance
import com.example.cashappstocks.network.StocksRepository
import com.example.cashappstocks.network.StocksRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * View Model for Main Class.
 */
class MainViewModel(private val stockRepo: StocksRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    fun getData() {
        viewModelScope.launch {
            val result = stockRepo.getStocks()
            when (result) {
                is Result.Loading -> _uiState.value = UIState(isLoading = true)
                is Result.Success -> _uiState.value = UIState(stocks = result.data)
                is Result.Error -> {
                    val errorCategory = when (result.code) {
                        204 -> ErrorCategory.NoData
                        else -> ErrorCategory.Generic
                    }
                    _uiState.value = UIState(isError = true, errprCategory = errorCategory)
                }
            }
        }
    }

    fun getFormattedDate(time: Long): String {
        return try {
            val sdf = SimpleDateFormat(DATE_FORMAT, Locale.CANADA)
            sdf.format(Date(time))
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    /**
     * Data Class used by UI Layer to display UI state.
     */
    data class UIState(
        val isLoading: Boolean = false,
        val stocks: List<Stock> = listOf(),
        val isError: Boolean? = null,
        val errprCategory: ErrorCategory? = null
    )
}


object CommonViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                stockRepo =
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

enum class ErrorCategory { NoData, Generic }