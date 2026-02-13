package com.kiosko.android.data.repository

import com.kiosko.android.data.mappers.toDomain
import com.kiosko.android.data.mappers.toDto
import com.kiosko.android.data.remote.api.KioskoApi
import com.kiosko.android.data.remote.dto.*
import com.kiosko.android.domain.models.*
import com.kiosko.android.domain.repository.*
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: KioskoApi
) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val response = api.login(LoginRequestDto(username, password))
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class StoreRepositoryImpl @Inject constructor(
    private val api: KioskoApi
) : StoreRepository {
    override suspend fun createStore(name: String, address: String?): Result<Store> {
        return try {
            val response = api.createStore(CreateStoreRequestDto(name, address))
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllStores(): Result<List<Store>> {
        return try {
            val response = api.getAllStores()
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class ProductRepositoryImpl @Inject constructor(
    private val api: KioskoApi
) : ProductRepository {
    override suspend fun createProduct(
        userId: String,
        name: String,
        barcode: String,
        price: Double,
        initialStock: Int
    ): Result<Product> {
        return try {
            val request = CreateProductRequestDto(userId, name, barcode, price, initialStock)
            val response = api.createProduct(request)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun restockProduct(
        userId: String,
        productId: String,
        quantity: Int
    ): Result<Product> {
        return try {
            val request = RestockRequestDto(userId, productId, quantity)
            val response = api.restockProduct(request)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStoreProducts(userId: String): Result<List<Product>> {
        return try {
            val response = api.getStoreProducts(userId)
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductByBarcode(userId: String, barcode: String): Result<Product> {
        return try {
            val response = api.getProductByBarcode(userId, barcode)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class SaleRepositoryImpl @Inject constructor(
    private val api: KioskoApi
) : SaleRepository {
    override suspend fun createSale(userId: String, items: List<SaleItem>): Result<Sale> {
        return try {
            val dtos = items.map { it.toDto() }
            val request = CreateSaleRequestDto(userId, dtos)
            val response = api.createSale(request)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}