package com.kiosko.android.data.remote.dto

data class CreateSaleRequestDto(
    val userId: String,
    val items: List<SaleItemDto>
)

data class SaleItemDto(
    val productId: String,
    val quantity: Int
)

data class SaleResponseDto(
    val id: String,
    val totalAmount: Double,
    val date: String,
    val itemCount: Int
)