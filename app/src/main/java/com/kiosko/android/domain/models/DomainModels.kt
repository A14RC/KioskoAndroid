package com.kiosko.android.domain.models

data class User(
    val userId: String,
    val username: String,
    val role: String,
    val storeId: String,
    val storeName: String
)

data class Store(
    val id: String,
    val name: String,
    val address: String?
)

data class Product(
    val id: String,
    val name: String,
    val barcode: String,
    val price: Double,
    val currentStock: Int
)

data class Sale(
    val id: String,
    val totalAmount: Double,
    val date: String,
    val itemCount: Int
)

data class SaleItem(
    val productId: String,
    val quantity: Int
)

data class SalesSummary(
    val totalSales: Double,
    val transactionCount: Int,
    val period: String
)