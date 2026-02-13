package com.kiosko.android.data.remote.dto

data class CreateProductRequestDto(
    val userId: String,
    val name: String,
    val barcode: String,
    val price: Double,
    val initialStock: Int
)

data class ProductResponseDto(
    val id: String,
    val name: String,
    val barcode: String,
    val price: Double,
    val currentStock: Int
)

data class RestockRequestDto(
    val userId: String,
    val productId: String,
    val quantityToAdd: Int
)