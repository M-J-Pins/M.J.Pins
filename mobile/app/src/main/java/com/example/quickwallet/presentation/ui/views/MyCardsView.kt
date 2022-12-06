package com.example.quickwallet.presentation.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Chip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.compose.AppTheme
import com.example.quickwallet.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MyCardsView() {

    AppTheme {
        val focusManager = LocalFocusManager.current
        Scaffold(modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { focusManager.clearFocus(true) }
            )
            .fillMaxSize(), topBar = {
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
        }, bottomBar = {
            val selectedIndex = remember { mutableStateOf(1) }
            NavigationBar(
                containerColor = Color.Transparent, tonalElevation = 8.dp


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
                                2 -> Icon(
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

        }) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(1f)
            ) {

                val showRecent by remember { mutableStateOf(true) }

                val leftPadding = createGuidelineFromAbsoluteLeft(0.044f)
                val rightPadding = createGuidelineFromAbsoluteRight(0.044f)
                val addLeftPadding = createGuidelineFromAbsoluteLeft(0.283f)
                val addRightPadding = createGuidelineFromAbsoluteRight(0.283f)
                var addTopPadding by remember {
                    mutableStateOf(createGuidelineFromTop(0.193f))
                }
                var addBottomPadding by remember {
                    mutableStateOf(createGuidelineFromBottom(0.754f))
                }
                LaunchedEffect(showRecent) {
                    addTopPadding = createGuidelineFromTop(0.132f)
                    addBottomPadding = createGuidelineFromBottom(0.815f)
                }
                val recentCategoryRightPadding = createGuidelineFromAbsoluteRight(0.644f)
                val recentCategoryTopPadding = createGuidelineFromTop(0.123f)
                val recentCategoryBottomPadding = createGuidelineFromBottom(0.833f)
                val (search, addBtn, btmDiv, recentCategory, lazyRow) = createRefs()

                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
                Divider(modifier = Modifier.constrainAs(btmDiv) {
                    bottom.linkTo(parent.bottom)
                }, thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
                TextField(modifier = Modifier.constrainAs(search) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(leftPadding)
                    end.linkTo(rightPadding)
                    width = Dimension.fillToConstraints

                }, value = "", onValueChange = {}, leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, "")
                }, trailingIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(painter = painterResource(id = R.drawable.filter_alt), "")
                    }
                }, placeholder = {
                    Text(
                        text = "Поиск",
                        fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
                        fontWeight = FontWeight.W500,
                        fontSize = 16.sp,
                        fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                    colors = TextFieldDefaults
                        .textFieldColors(containerColor = Color.Transparent),
                    singleLine = true
                )
                OutlinedButton(
                    modifier = Modifier
                        .constrainAs(addBtn) {
                            start.linkTo(addLeftPadding)
                            end.linkTo(addRightPadding)
                            top.linkTo(addTopPadding)
                            bottom.linkTo(addBottomPadding)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    onClick = { /*TODO*/ },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Box(modifier = Modifier.constrainAs(recentCategory) {
                    start.linkTo(leftPadding)
                    end.linkTo(recentCategoryRightPadding)
                    width = Dimension.fillToConstraints
                    top.linkTo(recentCategoryTopPadding)
                    bottom.linkTo(recentCategoryBottomPadding)
                    height = Dimension.fillToConstraints
                }) {
                    Text(
                        text = "Недавние категории: ",
                        fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
                        fontWeight = FontWeight.W400,
                        fontSize = 11.sp,
                        fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                val categoryBtns by remember { mutableStateOf(listOf("еда","красота","медицина")) }
                LazyRow(modifier = Modifier.constrainAs(lazyRow) {
                    start.linkTo(recentCategory.end, 8.dp)
                    end.linkTo(leftPadding)
                    width = Dimension.fillToConstraints
                }) {
                    items(categoryBtns.size){
                        
                    }
                }

            }
        }
    }
}