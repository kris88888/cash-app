package com.example.cashappstocks.network.data

import android.util.Log
import com.example.cashappstocks.network.StockService
import com.example.cashappstocks.network.data.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface StocksRepository {
    suspend fun getStocks(): Flow<Result<List<Stock>>>
}

class StocksRepositoryImpl(private val service: StockService) : StocksRepository {

    private val TAG = "KRIS"

    override suspend fun getStocks(): Flow<Result<List<Stock>>> {
        return flow {
            emit(Result.Loading())
            try {
                service.getPortfolio().apply {
                    Log.d(TAG, "getStocks: MAKING CALL AT ${Thread.currentThread().name}")
                    if (this.isSuccessful) {
                        this.body()?.let {
                            if (it.stocks.isEmpty()) {
                                emit(Result.Error("Empty", null))
                            } else {
                                emit(Result.Success(it.stocks))
                            }
                        } ?: kotlin.run {
                            emit(Result.Error("Empty Body"))
                        }
                    } else {
                        emit(Result.Error(message = this.message()))
                    }
                }
            } catch (ex: Throwable) {
                ex.printStackTrace()
                emit(Result.Error(message = ex.message ?: "Error Occured", exception = ex))
            }
        }
    }
}