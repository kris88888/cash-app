package com.example.cashappstocks.network.data


data class ApiResponse(
    val stocks: List<Stock>
)

data class Stock(
    val ticker: String,
    val name: String,
    val currency: String,
    val current_price_cents: String,
    val quantity: Int?,
    val current_price_timestamp: Long
)