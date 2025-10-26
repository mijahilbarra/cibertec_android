package com.cibertec.barber_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cibertec.barber_project.ui.components.molecules.SwipeableAppointmentCard
import com.cibertec.barber_project.viewmodels.AppointmentViewModel

/**
 * Dictionary for appointment type translations
 */
val appointmentTypeTranslations = mapOf(
    "scheduled" to "programado",
    "walk-in" to "inmediato"
)

/**
 * AppointmentScreen - Screen component
 * Screen to display and manage appointments with real-time Firestore updates and swipe actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    onBackClick: () -> Unit,
    viewModel: AppointmentViewModel = viewModel()
) {
    val appointments by viewModel.appointments.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentFilter by viewModel.currentFilter.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Citas",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = currentFilter == "active",
                    onClick = { viewModel.setFilter("active") },
                    label = { Text("Activas") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                
                FilterChip(
                    selected = currentFilter == "cancelled",
                    onClick = { viewModel.setFilter("cancelled") },
                    label = { Text("Canceladas") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                )
                
                FilterChip(
                    selected = currentFilter == "completed",
                    onClick = { viewModel.setFilter("completed") },
                    label = { Text("Completadas") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
            }
            
            // Content
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    isLoading -> {
                        // Loading state
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    error != null -> {
                        // Error state
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Error",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error ?: "Error desconocido",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    appointments.isEmpty() -> {
                        // Empty state
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = when (currentFilter) {
                                    "cancelled" -> "No hay citas canceladas"
                                    "completed" -> "No hay citas completadas"
                                    else -> "No hay citas activas"
                                },
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = when (currentFilter) {
                                    "cancelled" -> "Las citas canceladas aparecerán aquí"
                                    "completed" -> "Las citas completadas aparecerán aquí"
                                    else -> "Las citas pendientes y en proceso aparecerán aquí"
                                },
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    else -> {
                        // Display appointments list (sorted: processing first, then pending)
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                // Section headers
                                if (currentFilter == "active") {
                                    val processingCount = appointments.count { it.status == "processing" }
                                    val pendingCount = appointments.count { it.status == "pending" }
                                    
                                    if (processingCount > 0) {
                                        Text(
                                            text = "En Proceso ($processingCount)",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                            
                            // Processing appointments (shown first)
                            items(
                                items = appointments.filter { it.status == "processing" },
                                key = { it.id }
                            ) { appointment ->
                                SwipeableAppointmentCard(
                                    appointment = appointment,
                                    onStatusChange = { newStatus ->
                                        viewModel.updateAppointmentStatus(appointment.id, newStatus)
                                    }
                                )
                            }
                            
                            // Pending section header
                            if (currentFilter == "active") {
                                val pendingCount = appointments.count { it.status == "pending" }
                                if (pendingCount > 0) {
                                    item {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = "Pendientes ($pendingCount)",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                            
                            // Pending, cancelled, or completed appointments
                            items(
                                items = appointments.filter { 
                                    it.status != "processing" 
                                },
                                key = { it.id }
                            ) { appointment ->
                                SwipeableAppointmentCard(
                                    appointment = appointment,
                                    onStatusChange = { newStatus ->
                                        viewModel.updateAppointmentStatus(appointment.id, newStatus)
                                    }
                                )
                            }
                            
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
