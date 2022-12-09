package com.example.quickwallet.presentation.ui.views.addcard

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.quickwallet.presentation.ui.views.camera.BarcodeCameraView
import com.example.quickwallet.presentation.viewmodel.CardViewModel

@Composable
fun ScanBarcodeScreen(
    cardsViewModel: CardViewModel,
    navController: NavController,
){
    BarcodeCameraView(cardViewModel = cardsViewModel, navController = navController )
}