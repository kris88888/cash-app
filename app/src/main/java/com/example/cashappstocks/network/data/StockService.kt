package com.example.cashappstocks.network

import com.example.cashappstocks.network.data.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

private const val GET_PORTFOLIO_PATH = "/cash-homework/cash-stocks-api/portfolio.json"

interface StockService {
    @GET(GET_PORTFOLIO_PATH)
    suspend fun getPortfolio(): Response<ApiResponse>
}

