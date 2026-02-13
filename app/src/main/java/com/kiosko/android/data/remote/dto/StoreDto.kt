package com.kiosko.android.data.remote.dto

data class CreateStoreRequestDto(
    val name: String,
    val address: String?
)

data class StoreResponseDto(
    val id: String,
    val name: String,
    val address: String?
)