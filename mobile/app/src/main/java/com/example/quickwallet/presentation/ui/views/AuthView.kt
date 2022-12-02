package com.example.quickwallet.presentation.ui.views


import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quickwallet.presentation.ui.theme.QuickWalletTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AuthView(
    phone: String,
    code: String,
    onPhoneChanged: (String) -> Unit,
    onCodeChanged: (String) -> Unit,
    isPhoneSent: Boolean,
    isPhoneNumberError: Boolean,
    sendPhoneNumber: ()->Unit,
    sendPhoneAuth: ()->Unit,
    isTokenReceived: Boolean,
    navController: NavController,
    ) {
    QuickWalletTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = phone,
                    onValueChange = { onPhoneChanged(it) },
                    enabled = !isPhoneSent
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (!isPhoneSent && !isPhoneNumberError) {
                    Button(onClick = {
                        sendPhoneNumber()
                    }) {
                        Text(text = "send phone")
                    }
                } else {
                    TextField(value = code, onValueChange = { onCodeChanged(it) })
                    Button(onClick = {
                        sendPhoneAuth()
                    }) {
                        Text(text = "send phone")
                    }
                    if (isTokenReceived) {
                        navController.navigate("text")
                    }
                }
            }
        }
    }
}
