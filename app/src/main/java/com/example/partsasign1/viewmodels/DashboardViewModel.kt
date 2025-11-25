package com.example.partsasign1.viewmodels


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.partsasign1.model.DashboardStats

class DashboardViewModel : ViewModel() {

    private val _stats = MutableStateFlow(
        DashboardStats(

            disponibles = 13,
            retirados = 12,
            pendientes = 2,
            total = 18
        )
    )
    val stats: StateFlow<DashboardStats> = _stats.asStateFlow()

}


