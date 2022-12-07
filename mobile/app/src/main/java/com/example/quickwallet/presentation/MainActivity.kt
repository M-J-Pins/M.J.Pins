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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickwallet.presentation.ui.views.*
import com.example.quickwallet.presentation.viewmodel.ActivityViewModel
import com.example.quickwallet.presentation.viewmodel.AuthViewModel
import com.example.quickwallet.presentation.viewmodel.CardViewModel
import com.example.quickwallet.utils.Constants
import com.google.accompanist.pager.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    private val activityViewModel by viewModels<ActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )


        setContent {
            val navController = rememberNavController()
            val authViewModel by viewModels<AuthViewModel>()
            val cardViewModel by viewModels<CardViewModel>()

            makeStatusBarTransparent()

            NavHost(
                navController = navController,
//                if (activityViewModel.isFirstExecution.value) "send-phone" else "fast-card"
                startDestination = "send-code"
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
//                    CameraView(
//                        navController = navController,
//                    )
                }
                composable("fast-card") {
                    FastCardView(
                        viewModel = cardViewModel,
                        navController = navController
                    )
                }
                composable("my-cards") {
                    MyCardsView()
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




