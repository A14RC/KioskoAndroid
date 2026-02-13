package com.kiosko.android.data.mappers

import com.kiosko.android.data.remote.dto.*
import com.kiosko.android.domain.models.*

fun LoginResponseDto.toDomain(): User {
    return User(
        userId = userId,
        username = username,
        role = role,
        storeId = storeId,
        storeName = storeName
    )
}

fun StoreResponseDto.toDomain(): Store {
    return Store(
        id = id,
        name = name,
        address = address
    )
}

fun ProductResponseDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        barcode = barcode,
        price = price,
        currentStock = currentStock
    )
}

fun SaleResponseDto.toDomain(): Sale {
    return Sale(
        id = id,
        totalAmount = totalAmount,
        date = date,
        itemCount = itemCount
    )
}

fun SaleItem.toDto(): SaleItemDto {
    return SaleItemDto(
        productId = productId,
        quantity = quantity
    )
}

fun SalesSummaryResponseDto.toDomain(): SalesSummary {
    return SalesSummary(
        totalSales = totalSales,
        transactionCount = transactionCount,
        period = period
    )
}