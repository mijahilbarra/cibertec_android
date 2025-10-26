package com.cibertec.barber_project.data.repositories

import com.cibertec.barber_project.data.models.Appointment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

/**
 * AppointmentRepository - Repository
 * Handles all Firestore operations for appointments collection
 */
class AppointmentRepository {
    
    private val firestore = FirebaseFirestore.getInstance()
    private val appointmentsCollection = firestore.collection("appointments")
    
    /**
     * Fetch all appointments ordered by date descending
     */
    suspend fun getAllAppointments(): Result<List<Appointment>> {
        return try {
            val snapshot = appointmentsCollection
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val appointments = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Appointment::class.java)?.copy(id = doc.id)
            }
            
            Result.success(appointments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Fetch appointments by status
     */
    suspend fun getAppointmentsByStatus(status: String): Result<List<Appointment>> {
        return try {
            val snapshot = appointmentsCollection
                .whereEqualTo("status", status)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val appointments = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Appointment::class.java)?.copy(id = doc.id)
            }
            
            Result.success(appointments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Listen to real-time updates of all appointments
     */
    fun observeAppointments(
        onSuccess: (List<Appointment>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return appointmentsCollection
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val appointments = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Appointment::class.java)?.copy(id = doc.id)
                    }
                    onSuccess(appointments)
                }
            }
    }
    
    /**
     * Update appointment status
     */
    suspend fun updateAppointmentStatus(appointmentId: String, newStatus: String): Result<Unit> {
        return try {
            appointmentsCollection.document(appointmentId)
                .update("status", newStatus)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

