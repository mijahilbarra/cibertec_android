package com.cibertec.barber_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.barber_project.data.models.Appointment
import com.cibertec.barber_project.data.repositories.AppointmentRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * AppointmentViewModel - ViewModel
 * Manages appointment data with real-time updates
 */
class AppointmentViewModel : ViewModel() {
    
    private val repository = AppointmentRepository()
    
    private val _allAppointments = MutableStateFlow<List<Appointment>>(emptyList())
    
    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _currentFilter = MutableStateFlow("active") // active, cancelled, completed
    val currentFilter: StateFlow<String> = _currentFilter.asStateFlow()
    
    private var listenerRegistration: ListenerRegistration? = null
    
    init {
        observeAppointments()
    }
    
    /**
     * Start listening to appointments collection
     */
    private fun observeAppointments() {
        viewModelScope.launch {
            listenerRegistration = repository.observeAppointments(
                onSuccess = { appointmentList ->
                    _allAppointments.value = appointmentList
                    applyFilter()
                    _isLoading.value = false
                    _error.value = null
                },
                onError = { exception ->
                    _error.value = exception.message ?: "Error desconocido"
                    _isLoading.value = false
                }
            )
        }
    }
    
    /**
     * Update appointment status
     */
    fun updateAppointmentStatus(appointmentId: String, newStatus: String) {
        viewModelScope.launch {
            val result = repository.updateAppointmentStatus(appointmentId, newStatus)
            if (result.isFailure) {
                _error.value = "Error al actualizar el estado"
            }
        }
    }
    
    /**
     * Set filter for appointments
     */
    fun setFilter(filter: String) {
        _currentFilter.value = filter
        applyFilter()
    }
    
    /**
     * Apply current filter to appointments
     */
    private fun applyFilter() {
        val filtered = when (_currentFilter.value) {
            "cancelled" -> _allAppointments.value.filter { it.status == "cancelled" }
            "completed" -> _allAppointments.value.filter { it.status == "completed" }
            else -> _allAppointments.value.filter { 
                it.status != "cancelled" && it.status != "completed" 
            }
        }
        
        // Sort: processing first, then pending
        _appointments.value = filtered.sortedWith(
            compareByDescending<Appointment> { it.status == "processing" }
                .thenByDescending { it.status == "pending" }
                .thenByDescending { it.date }
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}

