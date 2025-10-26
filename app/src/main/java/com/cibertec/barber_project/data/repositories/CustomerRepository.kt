package com.cibertec.barber_project.data.repositories

import com.cibertec.barber_project.data.models.Customer
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

/**
 * CustomerRepository - Repository
 * Handles all Firestore operations for chatbot_users collection
 */
class CustomerRepository {
    
    private val firestore = FirebaseFirestore.getInstance()
    private val customersCollection = firestore.collection("chatbot_users")
    
    /**
     * Listen to real-time updates of all customers
     */
    fun observeCustomers(
        onSuccess: (List<Customer>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return customersCollection
            .orderBy("lastInteraction", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val customers = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Customer::class.java)?.copy(id = doc.id)
                    }
                    onSuccess(customers)
                }
            }
    }
}

