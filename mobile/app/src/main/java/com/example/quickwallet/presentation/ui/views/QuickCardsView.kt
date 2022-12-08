package com.example.quickwallet.presentation.ui.views

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.quickwallet.domain.model.Card
import com.example.quickwallet.presentation.viewmodel.CardViewModel
import com.example.quickwallet.utils.glide.DEFAULT_RECIPE_IMAGE
import com.example.quickwallet.utils.glide.loadPicture
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalPagerApi::class
)
@Composable
fun QuickCardsView(
    cardsViewModel: CardViewModel,
    token: String
) {
    AppTheme {
        val locationPermissionState =
            rememberMultiplePermissionsState(
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        if (!locationPermissionState.allPermissionsGranted) {
            locationPermissionState.launchMultiplePermissionRequest()
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            var itemInd by remember { mutableStateOf(0) }
            Text(
                modifier = Modifier.padding(vertical = 24.dp),
                text = if (cardsViewModel.quickCards.value.isNotEmpty())
                    cardsViewModel.quickCards.value[itemInd].shopName else "",
                fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
                fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                color = MaterialTheme.colorScheme.onBackground,
            )
            VerticalPager(
                count = cardsViewModel.quickCards.value.size,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) { page ->
                itemInd = page
                Card(
                    modifier = Modifier
                        .requiredWidth(316.dp)
                        .requiredHeight(186.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (cardsViewModel.quickCards.value.isNotEmpty()){
                        cardsViewModel.quickCards.value[page].imageUrl?.let { url ->
                            val image =
                                loadPicture(url = url, defaultImage = DEFAULT_RECIPE_IMAGE).value
                            image?.let { img ->
                                Image(
                                    bitmap = img.asImageBitmap(),
                                    contentDescription = "Card",
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    contentScale = ContentScale.FillBounds,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

