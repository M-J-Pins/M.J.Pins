package com.example.quickwallet.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quickwallet.R
import com.example.quickwallet.presentation.navigation.Screen
import com.example.quickwallet.presentation.navigation.bottomNavigationScreens
import com.example.quickwallet.presentation.ui.views.*
import com.example.quickwallet.presentation.viewmodel.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    private val activityViewModel by viewModels<ActivityViewModel>()

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
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
            val photoViewModel = hiltViewModel<PhotoViewModel>()
            val shopsViewModel = hiltViewModel<ShopsViewModel>()


            makeStatusBarTransparent()

            val focusManager = LocalFocusManager.current

            Scaffold(modifier = Modifier
                .clickable(interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { focusManager.clearFocus(true) })
                .windowInsetsPadding(WindowInsets.statusBars)
                .fillMaxSize(), topBar = {
                if (!activityViewModel.isFirstExecution.value) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "Мои карты",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W700,
                                fontFamily = MaterialTheme.typography.headlineSmall.fontFamily
                            )
                        },
                    )
                }
            }, bottomBar = {
                if (!activityViewModel.isFirstExecution.value) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    NavigationBar(
                        containerColor = Color.Transparent, tonalElevation = 8.dp
                    ) {
                        bottomNavigationScreens.forEach { screen ->
                            NavigationBarItem(selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    when (screen.title) {
                                        "MyCards" -> Icon(
                                            modifier = Modifier.requiredSize(20.dp),
                                            painter = painterResource(id = R.mipmap.subscriptions),
                                            contentDescription = "",

                                            )
                                        "QuickCards" -> Icon(
                                            imageVector = Icons.Default.CreditCard, ""
                                        )
                                        "Wallet" -> Icon(
                                            imageVector = Icons.Default.AccountBalanceWallet, ""
                                        )
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.background,
                                    indicatorColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }

                }
            }) { scaffoldPadding ->
                NavHost(
                    modifier = Modifier.padding(scaffoldPadding), navController = navController,
                    startDestination =
//                    if (activityViewModel.isFirstExecution.value)
//                        Screen.AuthScreens.AuthStart.route
//                    else Screen.QuickWallet.QuickCards.route
                    Screen.QuickWallet.ScanCardFrontSurfaceScreen.route

                ) {
                    composable(Screen.AuthScreens.AuthStart.route) {
                        StartAuthScreen(navController = navController)
                    }
                    composable(Screen.AuthScreens.AuthPhoneRequest.route) {
                        SendPhoneNumberView(
                            viewModel = authViewModel,
                            navController = navController,
                        )
                    }
                    composable(Screen.AuthScreens.AuthRequest.route) {
                        SendCodeView(
                            viewModel = authViewModel,
                            activityViewModel = activityViewModel,
                            navController = navController
                        )
                    }
                    composable(Screen.QuickWallet.QuickCards.route) {
                        LaunchedEffect(Unit) {
                            cardViewModel.getQuickCards(activityViewModel.accessToken.value)
                        }
                        QuickCardsView(cardViewModel, activityViewModel.accessToken.value)
                    }
                    composable(Screen.QuickWallet.MyCards.route) {
                        MyCardsView(
                            activityViewModel.accessToken.value,
                            navController,
                            cardViewModel
                        )
                    }
                    composable(Screen.QuickWallet.ScanCardFrontSurfaceScreen.route) {
                        ScanCardFrontSurface(
                            photoViewModel,
                            shopsViewModel,
                            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjFhNGFhYjExLTcxYjktNGQ2Zi05N2YyLTgzYjM5YjAzOWZiMyIsImV4cCI6MTY3MzEyMzg0NH0.gxjk-K6OU8nXNTSbTXhDXuPMtW_b59lRpvCRpAWdy-4",
                            navController,
                            PhotoViewMode.IMAGE_CAPTURE
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
            color = Color.Transparent, darkIcons = useDarkIcons
        )
        onDispose {}
    }
}




