package com.cibertec.barber_project.data.models

import com.google.firebase.Timestamp

/**
 * Customer - Data model
 * Represents a customer (chatbot_users) document from Firestore
 */
data class Customer(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val chat: String = "",
    val createdAt: Timestamp? = null,
    val lastInteraction: Timestamp? = null
)

