package com.kiosko.android.presentation.screens.sales

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
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

    LaunchedEffect(saleSuccess) {
        if (saleSuccess) {
            Toast.makeText(context, "¡Venta Registrada con Éxito!", Toast.LENGTH_LONG).show()
            viewModel.resetState()
            onNavigateBack()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
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
            if (cart.isNotEmpty()) {
                Surface(
                    shadowElevation = 16.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total a Cobrar:", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                            Text("$${String.format("%.2f", totalAmount)}", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.confirmSale() },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50) // Verde éxito
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White)
                            } else {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("CONFIRMAR VENTA", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(horizontal = 16.dp)) {
            Text(
                "Catálogo de Productos",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(allProducts) { product ->
                    ProductSaleCard(
                        product = product,
                        qtyInCart = cart[product.id] ?: 0,
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
    val isSelected = qtyInCart > 0
    val isLowStock = product.currentStock < 10

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f) else Color.White
        ),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna Izquierda: Info del Producto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))

                // PRECIO GRANDE
                Text(
                    text = "$${product.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ✅ STOCK VISIBLE CON ALERTA
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isLowStock) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = "Stock Bajo",
                            tint = Color(0xFFFF9800), // Naranja alerta
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = "Stock: ${product.currentStock}",
                        fontSize = 14.sp,
                        fontWeight = if(isLowStock) FontWeight.Bold else FontWeight.Normal,
                        color = if (isLowStock) Color(0xFFFF9800) else Color.Gray
                    )
                }
            }

            // Columna Derecha: Controles (+ -)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (qtyInCart > 0) {
                    FilledIconButton(
                        onClick = onRemove,
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFFFFEBEE)) // Rojo claro
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Quitar", tint = Color.Red)
                    }

                    Text(
                        text = "$qtyInCart",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                FilledIconButton(
                    onClick = onAdd,
                    enabled = product.currentStock > qtyInCart,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (product.currentStock > qtyInCart) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Agregar",
                        tint = Color.White
                    )
                }
            }
        }
    }
}