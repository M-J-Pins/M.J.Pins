package com.example.quickwallet.presentation.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.example.quickwallet.R
import com.example.quickwallet.domain.model.cards
import com.example.quickwallet.presentation.viewmodel.CardViewModel
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun MyCardsView(
    token: String,
    navController: NavController,
    cardViewModel: CardViewModel
) {
    AppTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = cardViewModel.searchText.value,
                onValueChange = { cardViewModel.onSearchTextChanged(it) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search, "",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter_alt),
                            "",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                placeholder = {
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
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(21.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Недавние категории: ",
                    fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
                    fontWeight = FontWeight.W400,
                    fontSize = 11.sp,
                    fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                    color = MaterialTheme.colorScheme.primary,
                )
                val categoryBtns by remember {
                    mutableStateOf(
                        listOf(
                            "еда",
                            "красота",
                            "медицина",
                            "еда",
                            "красота",
                            "медицина"
                        )
                    )
                }
                LazyRow() {
                    items(categoryBtns.size) {
                        Chip(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onClick = { /*TODO*/ },
                            colors = ChipDefaults.chipColors(
                                backgroundColor = MaterialTheme.colorScheme.secondary,
                            )
                        ) {
                            Text(
                                text = categoryBtns[it],
                                color = MaterialTheme.colorScheme.background,
                                fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
                                fontWeight = FontWeight.W400,
                                fontSize = 11.sp,
                                fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                            )
                        }
                    }
                }
            }
            OutlinedButton(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 102.dp)
                    .fillMaxWidth(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cards.size) {
                    Card(
                        modifier = Modifier
                            .requiredWidth(316.dp)
                            .requiredHeight(186.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            ""
                        )
                    }
                }
            }
        }

    }
}

