package com.kiosko.android.data.remote.dto

data class LoginRequestDto(
    val username: String,
    val password: String
)

data class LoginResponseDto(
    val userId: String,
    val username: String,
    val role: String,
    val storeId: String,
    val storeName: String
)