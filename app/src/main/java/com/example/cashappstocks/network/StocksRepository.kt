package com.example.cashappstocks.network

import com.example.cashappstocks.network.data.Stock
import com.example.cashappstocks.network.data.Result

interface StocksRepository {
    suspend fun getStocks(): Result<List<Stock>>
}

class StocksRepositoryImpl(private val service: StockService) : StocksRepository {

    override suspend fun getStocks(): Result<List<Stock>> {
        try {
            service.getPortfolio().apply {
                val result = if (this.isSuccessful) {
                    this.body()?.let {
                        if (it.stocks.isEmpty()) {
                            Result.Error(code = 204, "Empty")
                        } else {
                            Result.Success(it.stocks)
                        }
                    } ?: kotlin.run {
                        Result.Error(this.code(), "Response Error")
                    }
                } else {
                    Result.Error(this.code(),message = this.message())
                }
                return result
            }
        } catch (ex: Throwable) {
            ex.printStackTrace()
            return Result.Error(
                code = 500,
                message = ex.message ?: "Error Occurred while making service",
                exception = ex
            )
        }
    }
}