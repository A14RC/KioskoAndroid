package com.kiosko.android.presentation.screens.sales

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiosko.android.domain.models.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(
    viewModel: SalesViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val allProducts by viewModel.allProducts
    val cart by viewModel.cart
    val totalAmount by viewModel.totalAmount
    val isLoading by viewModel.isLoading
    val saleSuccess by viewModel.saleSuccess
    val error by viewModel.error

    val context = LocalContext.current

    // Efecto para mostrar mensajes
    LaunchedEffect(saleSuccess) {
        if (saleSuccess) {
            Toast.makeText(context, "¡Venta Registrada con Éxito!", Toast.LENGTH_LONG).show()
            viewModel.resetState()
            onNavigateBack() // Volvemos al home al terminar
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.resetState() // Limpiamos el error para que no salga de nuevo
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Venta", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // Barra Inferior de Total y Cobrar
            if (cart.isNotEmpty()) {
                Surface(
                    shadowElevation = 16.dp,
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total a Cobrar:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text("$${String.format("%.2f", totalAmount)}", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4CAF50))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.confirmSale() },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White)
                            } else {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("CONFIRMAR VENTA", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text("Seleccionar Productos", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(allProducts) { product ->
                    ProductSaleCard(
                        product = product,
                        qtyInCart = cart[product] ?: 0,
                        onAdd = { viewModel.addToCart(product) },
                        onRemove = { viewModel.removeFromCart(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductSaleCard(
    product: Product,
    qtyInCart: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (qtyInCart > 0) Color(0xFFE8F5E9) else Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("$${product.price}", color = Color.Gray)
            }

            // Controles de Cantidad (+ -)
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (qtyInCart > 0) {
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Default.Remove, contentDescription = "Quitar", tint = Color.Red)
                    }
                    Text(
                        text = "$qtyInCart",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                IconButton(
                    onClick = onAdd,
                    enabled = product.currentStock > qtyInCart
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Agregar",
                        tint = if (product.currentStock > qtyInCart) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }
        }
    }
}