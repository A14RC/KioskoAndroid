package com.kiosko.android.presentation.screens.reports

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiosko.android.core.utils.SessionManager
import com.kiosko.android.domain.models.SalesSummary
import com.kiosko.android.domain.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: SaleRepository
) : ViewModel() {

    private val _summary = mutableStateOf<SalesSummary?>(null)
    val summary: State<SalesSummary?> = _summary

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadReport()
    }

    fun loadReport() {
        val user = SessionManager.currentUser ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getDailySummary(user.storeId)
            if (result.isSuccess) {
                _summary.value = result.getOrNull()
            }
            _isLoading.value = false
        }
    }
}