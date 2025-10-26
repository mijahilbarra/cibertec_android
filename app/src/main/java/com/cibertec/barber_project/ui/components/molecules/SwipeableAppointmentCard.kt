package com.cibertec.barber_project.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cibertec.barber_project.data.models.Appointment
import com.cibertec.barber_project.ui.screens.appointmentTypeTranslations
import java.text.SimpleDateFormat
import java.util.*

/**
 * SwipeableAppointmentCard - Molecule component
 * Displays appointment with swipe-to-action functionality using Material 3 SwipeToDismissBox
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableAppointmentCard(
    appointment: Appointment,
    onStatusChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Determine available actions based on status
    val (leftAction, rightAction) = when (appointment.status) {
        "pending" -> Pair("cancelled", "processing")
        "processing" -> Pair("cancelled", "completed")
        else -> Pair(null, null)
    }
    
    // Only enable swipe if actions are available
    if (leftAction == null && rightAction == null) {
        // No swipe actions available, render card directly
        AppointmentCardContent(appointment = appointment, modifier = modifier)
        return
    }
    
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    // Swiped right
                    rightAction?.let { onStatusChange(it) }
                    false // Return to center
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    // Swiped left
                    leftAction?.let { onStatusChange(it) }
                    false // Return to center
                }
                SwipeToDismissBoxValue.Settled -> false
            }
        }
    )
    
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier.padding(vertical = 8.dp),
        backgroundContent = {
            // Background that shows when swiping
            val direction = dismissState.dismissDirection
            val color = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Color(0xFF1E88E5) // Blue for right swipe
                SwipeToDismissBoxValue.EndToStart -> Color(0xFFE53935) // Red for left swipe
                else -> Color.Transparent
            }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 24.dp),
                contentAlignment = when (direction) {
                    SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                    SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                    else -> Alignment.Center
                }
            ) {
                when (direction) {
                    SwipeToDismissBoxValue.StartToEnd -> {
                        // Right swipe action indicator
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = when (rightAction) {
                                    "processing" -> Icons.Default.AccessTime
                                    "completed" -> Icons.Default.CheckCircle
                                    else -> Icons.Default.Check
                                },
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = when (rightAction) {
                                    "processing" -> "En proceso"
                                    "completed" -> "Completada"
                                    else -> ""
                                },
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    SwipeToDismissBoxValue.EndToStart -> {
                        // Left swipe action indicator
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Cancelar",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    else -> {}
                }
            }
        },
        enableDismissFromStartToEnd = rightAction != null,
        enableDismissFromEndToStart = leftAction != null
    ) {
        AppointmentCardContent(appointment = appointment)
    }
}

/**
 * AppointmentCardContent - Displays the appointment card UI
 */
@Composable
private fun AppointmentCardContent(
    appointment: Appointment,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Status badge and name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = appointment.userName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Surface(
                    color = getStatusColor(appointment.status),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = getStatusText(appointment.status),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Phone
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Teléfono",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = appointment.phone,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Date and time
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Fecha",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatDate(appointment.date?.toDate()),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "• ${appointment.timeSlot}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Type
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Tipo",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tipo: ${appointmentTypeTranslations[appointment.type] ?: appointment.type}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Swipe hint
            if (appointment.status == "pending" || appointment.status == "processing") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "← Desliza para cambiar estado →",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

/**
 * Get status color based on status value
 */
@Composable
private fun getStatusColor(status: String) = when (status) {
    "confirmed", "pending" -> MaterialTheme.colorScheme.secondary
    "processing" -> MaterialTheme.colorScheme.primary
    "cancelled" -> MaterialTheme.colorScheme.error
    "completed" -> MaterialTheme.colorScheme.tertiary
    else -> MaterialTheme.colorScheme.secondary
}

/**
 * Get status text in Spanish
 */
private fun getStatusText(status: String) = when (status) {
    "confirmed" -> "Confirmada"
    "pending" -> "Pendiente"
    "processing" -> "En proceso"
    "cancelled" -> "Cancelada"
    "completed" -> "Completada"
    else -> status
}

/**
 * Format Date to readable string
 */
private fun formatDate(date: Date?): String {
    if (date == null) return "Fecha no disponible"
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(date)
}

