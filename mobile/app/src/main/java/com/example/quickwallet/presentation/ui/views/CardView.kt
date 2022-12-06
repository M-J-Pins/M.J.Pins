package com.example.quickwallet.presentation.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Card
import androidx.compose.material3.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.example.quickwallet.R
import com.example.quickwallet.domain.model.Card
import com.example.quickwallet.presentation.viewmodel.CardViewModel
import com.example.quickwallet.utils.glide.DEFAULT_RECIPE_IMAGE
import com.example.quickwallet.utils.glide.loadPicture
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.math.absoluteValue
import com.example.quickwallet.domain.model.cards

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FastCardView(
    viewModel: CardViewModel,
    navController: NavController,
//    token: Strin
) {
    val items = listOf("quick", "cards", "wallet")
    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(94.dp),
                    backgroundColor = Color.Transparent,
                    contentPadding = PaddingValues(horizontal = 113.dp),
                    elevation = 1.dp
                ) {
                    Text(
                        text = "Quick wallet",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
                        fontFamily = Font(R.font.pt_root_ui_bold).toFontFamily(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.W700

                    )
                }
            },
            bottomBar = {
                val selectedIndex = remember { mutableStateOf(1) }
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(100.dp),
                    containerColor = Color.Transparent,
                    tonalElevation = 8.dp


                ) {
                    repeat(3) {
                        NavigationBarItem(
                            selected = selectedIndex.value == it,
                            onClick = { selectedIndex.value = it },
                            icon = {
                                when (it) {
                                    0 -> Icon(
                                        modifier = Modifier.requiredSize(20.dp),
                                        painter = painterResource(id = R.drawable.subscriptions),
                                        contentDescription = "",

                                    )
                                    1 -> Icon(imageVector = Icons.Default.CreditCard, "")
                                    2 -> Icon(imageVector = Icons.Default.AccountBalanceWallet, "")
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
        ) {
            CardWheel()
//            FastCard(card = cards[0])
        }
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CardWheel() {

Column(modifier = Modifier){
    VerticalPager(
        count = 3,
        // Add 32.dp horizontal padding to 'center' the pages
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 220.dp)
            .fillMaxWidth()
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
                        start = 0.2f,
                        stop = 1f,
                        fraction = 3f - 3 * pageOffset.coerceIn(0f, 3f)
                    )
                }
                .aspectRatio(1f)
        ) {
            FastCard(card = cards[page])
        }
    }
}

}


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FastCard(card: Card) {
    Card(
        modifier = Modifier
            .requiredWidth(316.dp)
            .requiredHeight(186.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        card.imageUrl?.let { url ->
            val image = loadPicture(url = url, defaultImage = DEFAULT_RECIPE_IMAGE).value
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


