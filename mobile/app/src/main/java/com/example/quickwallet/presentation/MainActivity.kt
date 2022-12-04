package com.example.quickwallet.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickwallet.R
import com.example.quickwallet.presentation.ui.views.AuthView
import com.example.quickwallet.presentation.ui.views.CameraView
import com.example.quickwallet.presentation.viewmodel.AuthViewModel
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

        setContent {
            val navController = rememberNavController()
            makeStatusBarTransparent()
            NavHost(navController = navController, startDestination = "auth") {
                composable("auth") {
                    val viewModel = hiltViewModel<AuthViewModel>()
                    AuthView(
                        phone = viewModel.phoneNumber.value,
                        codeCells = viewModel.codeCells.value,
                        onPhoneChanged = viewModel::onPhoneNumberChanged,
                        onCodeCellChanged = viewModel::onCodeCellChange,
                        isPhoneSendScreen = viewModel.isPhoneSendScreen.value,
                        isPhoneNumberError = viewModel.phoneNumberError.value,
                        onPhoneSendScreen = viewModel::onPhoneSendScreen,
                        sendPhoneNumber = viewModel::sendPhoneNumber,
                        sendPhoneAuth = viewModel::sendPhoneAuth,
                        isTokenReceived = viewModel.tokenReceived.value,
                        navController = navController,
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
                composable("pager") {
                    Box(
                        Modifier
                            .background(Color.White)
                            .fillMaxSize(1f)
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.gradient),
                            contentDescription = null
                        )
                    }
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


@OptIn(ExperimentalPagerApi::class)
@Composable
fun CardWheel() {
    VerticalPager(
        count = 10,
        // Add 32.dp horizontal padding to 'center' the pages
        contentPadding = PaddingValues(vertical = 200.dp),

        modifier = Modifier.fillMaxSize()
    ) { page ->
        Card(
            Modifier
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                    // We animate the scaleX + scaleY, between 85% and 100%
                    lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Box(
                Modifier
                    .background(color = Color.Red)
            )
        }
    }
}

