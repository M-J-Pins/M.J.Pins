package com.example.quickwallet.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickwallet.presentation.ui.views.SendPhoneNumberView
import com.example.quickwallet.presentation.ui.views.CameraView
import com.example.quickwallet.presentation.ui.views.FastCardView
import com.example.quickwallet.presentation.ui.views.SendCodeView
import com.example.quickwallet.presentation.viewmodel.AuthViewModel
import com.example.quickwallet.presentation.viewmodel.CardViewModel
import com.example.quickwallet.utils.Constants
import com.google.accompanist.pager.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.absoluteValue


@AndroidEntryPoint

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val isToken =
            applicationContext.getSharedPreferences(Constants.sharedPreferencesStorageName, MODE_PRIVATE)
                .contains("token")

        setContent {
            val navController = rememberNavController()
            val authViewModel by viewModels<AuthViewModel>()
            val cardViewModel by viewModels<CardViewModel>()
            val token = remember { mutableStateOf("") }

            makeStatusBarTransparent()

            if (isToken) {
                token.value = applicationContext.getSharedPreferences(
                    Constants.sharedPreferencesStorageName,
                    MODE_PRIVATE
                ).getString("token", "") ?: ""
            }
//if (!isToken) "send-phone" else "token"
            NavHost(
                navController = navController,
                startDestination = "fast-card"
            ) {
                composable("send-phone") {
                    SendPhoneNumberView(
                        viewModel = authViewModel,
                        navController = navController,
                    )
                }
                composable("send-code") {
                    SendCodeView(
                        viewModel = authViewModel,
                        navController = navController
                    )
                }
                composable("text") {
                    Text(text = "Success")
                }
                composable("camera-view") {
                    CameraView(
                        navController = navController,
                    )
                }
                composable("fast-card") {
//                    token.value = applicationContext.getSharedPreferences(
//                        Constants.sharedPreferencesStorageName,
//                        MODE_PRIVATE
//                    ).getString("token", "") ?: ""
                    FastCardView(
                        viewModel = cardViewModel,
                        navController = navController
                    )
                }
            }
        }
    }

}


@Composable
fun makeStatusBarTransparent() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
        onDispose {}
    }
}




