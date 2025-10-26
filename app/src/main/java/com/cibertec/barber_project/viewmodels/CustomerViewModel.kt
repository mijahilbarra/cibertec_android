package com.cibertec.barber_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.barber_project.data.models.Customer
import com.cibertec.barber_project.data.repositories.CustomerRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * CustomerViewModel - ViewModel
 * Manages customer data with real-time updates
 */
class CustomerViewModel : ViewModel() {
    
    private val repository = CustomerRepository()
    
    private val _customers = MutableStateFlow<List<Customer>>(emptyList())
    val customers: StateFlow<List<Customer>> = _customers.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private var listenerRegistration: ListenerRegistration? = null
    
    init {
        observeCustomers()
    }
    
    /**
     * Start listening to chatbot_users collection
     */
    private fun observeCustomers() {
        viewModelScope.launch {
            listenerRegistration = repository.observeCustomers(
                onSuccess = { customerList ->
                    _customers.value = customerList
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
    
    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}

