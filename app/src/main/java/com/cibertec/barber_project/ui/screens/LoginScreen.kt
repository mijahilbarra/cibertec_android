package com.cibertec.barber_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cibertec.barber_project.ui.components.atoms.GoogleSignInButton
import com.cibertec.barber_project.ui.components.molecules.AppLogo

/**
 * LoginScreen - Screen component
 * Login screen with Google Sign In
 */
@Composable
fun LoginScreen(
    isLoading: Boolean,
    onGoogleSignInClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo and Title
            AppLogo()
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Google Sign In Button
            GoogleSignInButton(
                onClick = onGoogleSignInClick,
                isLoading = isLoading
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Terms and conditions
            Text(
                text = "By signing in, you agree to our Terms of Service\nand Privacy Policy",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}
