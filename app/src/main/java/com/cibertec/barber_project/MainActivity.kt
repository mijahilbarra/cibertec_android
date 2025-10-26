package com.cibertec.barber_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cibertec.barber_project.ui.screens.AppointmentScreen
import com.cibertec.barber_project.ui.screens.CustomersScreen
import com.cibertec.barber_project.ui.screens.HomeScreen
import com.cibertec.barber_project.ui.theme.Barber_projectTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    
    private lateinit var auth: FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        auth = FirebaseAuth.getInstance()
        
        // Check if user is logged in, if not redirect to login
        if (auth.currentUser == null) {
            navigateToLogin()
            return
        }
        
        enableEdgeToEdge()
        setContent {
            Barber_projectTheme {
                MainNavigation(
                    userName = auth.currentUser?.displayName ?: "User",
                    userEmail = auth.currentUser?.email ?: "",
                    onSignOut = { signOut() }
                )
            }
        }
    }
    
    private fun signOut() {
        auth.signOut()
        navigateToLogin()
    }
    
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun MainNavigation(
    userName: String,
    userEmail: String,
    onSignOut: () -> Unit
) {
    var currentScreen by remember { mutableStateOf("home") }
    
    when (currentScreen) {
        "home" -> HomeScreen(
            userName = userName,
            userEmail = userEmail,
            onSignOutClick = onSignOut,
            onCustomersClick = { currentScreen = "customers" },
            onAppointmentClick = { currentScreen = "appointment" }
        )
        "customers" -> CustomersScreen(
            onBackClick = { currentScreen = "home" }
        )
        "appointment" -> AppointmentScreen(
            onBackClick = { currentScreen = "home" }
        )
    }
}
