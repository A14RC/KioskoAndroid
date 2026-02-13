package com.kiosko.android.presentation.screens.sales

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiosko.android.core.utils.SessionManager
import com.kiosko.android.domain.models.Product
import com.kiosko.android.domain.models.SaleItem
import com.kiosko.android.domain.repository.ProductRepository
import com.kiosko.android.domain.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val saleRepository: SaleRepository
) : ViewModel() {

    // Lista de productos disponibles para buscar
    private val _allProducts = mutableStateOf<List<Product>>(emptyList())
    val allProducts: State<List<Product>> = _allProducts

    // Carrito de compras (Producto + Cantidad)
    private val _cart = mutableStateOf<Map<Product, Int>>(emptyMap())
    val cart: State<Map<Product, Int>> = _cart

    private val _totalAmount = mutableStateOf(0.0)
    val totalAmount: State<Double> = _totalAmount

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _saleSuccess = mutableStateOf(false)
    val saleSuccess: State<Boolean> = _saleSuccess

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    init {
        loadProducts()
    }

    private fun loadProducts() {
        val userId = SessionManager.currentUser?.userId ?: return
        viewModelScope.launch {
            val result = productRepository.getStoreProducts(userId)
            if (result.isSuccess) {
                _allProducts.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    fun addToCart(product: Product) {
        val currentQty = _cart.value[product] ?: 0
        if (currentQty < product.currentStock) {
            val newCart = _cart.value.toMutableMap()
            newCart[product] = currentQty + 1
            _cart.value = newCart
            calculateTotal()
        } else {
            _error.value = "Stock insuficiente para ${product.name}"
        }
    }

    fun removeFromCart(product: Product) {
        val currentQty = _cart.value[product] ?: 0
        if (currentQty > 0) {
            val newCart = _cart.value.toMutableMap()
            if (currentQty == 1) {
                newCart.remove(product)
            } else {
                newCart[product] = currentQty - 1
            }
            _cart.value = newCart
            calculateTotal()
        }
    }

    private fun calculateTotal() {
        var total = 0.0
        _cart.value.forEach { (product, qty) ->
            total += product.price * qty
        }
        _totalAmount.value = total
    }

    fun confirmSale() {
        val userId = SessionManager.currentUser?.userId ?: return
        if (_cart.value.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Convertir carrito al formato que pide la API
            val saleItems = _cart.value.map { (product, qty) ->
                SaleItem(productId = product.id, quantity = qty)
            }

            val result = saleRepository.createSale(userId, saleItems)

            if (result.isSuccess) {
                _saleSuccess.value = true
                _cart.value = emptyMap()
                calculateTotal()
                loadProducts() // Recargar para actualizar stock
            } else {
                _error.value = "Error al procesar venta: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    fun resetState() {
        _saleSuccess.value = false
        _error.value = null
    }
}