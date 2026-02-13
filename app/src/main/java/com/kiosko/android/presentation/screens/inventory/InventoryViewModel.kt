package com.kiosko.android.presentation.screens.inventory

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiosko.android.core.utils.SessionManager
import com.kiosko.android.domain.models.Product
import com.kiosko.android.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    init {
        loadProducts()
    }

    fun loadProducts() {
        val user = SessionManager.currentUser
        if (user == null) {
            _error.value = "No hay sesión activa"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getStoreProducts(user.userId)

            if (result.isSuccess) {
                _products.value = result.getOrNull() ?: emptyList()
            } else {
                _error.value = "Error al cargar productos: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }
}