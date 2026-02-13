package com.kiosko.android.domain.repository

import com.kiosko.android.domain.models.*

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
}

interface StoreRepository {
    suspend fun createStore(name: String, address: String?): Result<Store>
    suspend fun getAllStores(): Result<List<Store>>
}

interface ProductRepository {
    suspend fun createProduct(userId: String, name: String, barcode: String, price: Double, initialStock: Int): Result<Product>
    suspend fun restockProduct(userId: String, productId: String, quantity: Int): Result<Product>
    suspend fun getStoreProducts(userId: String): Result<List<Product>>
    suspend fun getProductByBarcode(userId: String, barcode: String): Result<Product>
}

interface SaleRepository {
    suspend fun createSale(userId: String, items: List<SaleItem>): Result<Sale>
}