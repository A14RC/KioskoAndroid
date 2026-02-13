package com.kiosko.android.data.remote.api

import com.kiosko.android.data.remote.dto.*
import retrofit2.http.*

interface KioskoApi {

    // Auth
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    // Stores
    @POST("api/v1/stores")
    suspend fun createStore(@Body request: CreateStoreRequestDto): StoreResponseDto

    @GET("api/v1/stores")
    suspend fun getAllStores(): List<StoreResponseDto>

    // Products
    @POST("api/v1/products")
    suspend fun createProduct(@Body request: CreateProductRequestDto): ProductResponseDto

    @PATCH("api/v1/products/restock")
    suspend fun restockProduct(@Body request: RestockRequestDto): ProductResponseDto

    @GET("api/v1/products")
    suspend fun getStoreProducts(@Query("userId") userId: String): List<ProductResponseDto>

    @GET("api/v1/products/scan")
    suspend fun getProductByBarcode(
        @Query("userId") userId: String,
        @Query("barcode") barcode: String
    ): ProductResponseDto

    // Sales
    @POST("api/v1/sales")
    suspend fun createSale(@Body request: CreateSaleRequestDto): SaleResponseDto

    // Sales Summary (NUEVO)
    @GET("api/v1/sales/summary")
    suspend fun getDailySummary(
        @Query("storeId") storeId: String,
        @Query("date") date: String? = null // null = hoy
    ): SalesSummaryResponseDto
}