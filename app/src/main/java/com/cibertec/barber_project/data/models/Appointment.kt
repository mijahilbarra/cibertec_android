package com.cibertec.barber_project.data.models

import com.google.firebase.Timestamp

/**
 * Appointment - Data model
 * Represents an appointment document from Firestore
 */
data class Appointment(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val phone: String = "",
    val date: Timestamp? = null,
    val timeSlot: String = "",
    val status: String = "",
    val type: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)

