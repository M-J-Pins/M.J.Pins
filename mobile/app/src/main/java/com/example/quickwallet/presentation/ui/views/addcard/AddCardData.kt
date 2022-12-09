package com.example.quickwallet.presentation.ui.views.addcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.compose.AppTheme
import com.example.quickwallet.presentation.ui.theme.fontMedium
import com.example.quickwallet.presentation.viewmodel.CardViewModel
import com.example.quickwallet.presentation.viewmodel.ShopsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardData(
    cardViewModel: CardViewModel,
    shopsViewModel: ShopsViewModel
) {
    AppTheme {
        val contextForToast = LocalContext.current.applicationContext

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {
            Card(modifier = Modifier.fillMaxWidth().requiredHeight(102.dp)) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(88.dp)
                        .align(Alignment.CenterHorizontally),
                    bitmap = getBarcodeBitmap(cardViewModel.barcodeData).asImageBitmap(),
                    contentDescription = "some useful description",
                )

            }

            shopsViewModel.fetchShopCategories()
            shopsViewModel.fetchAllShops()

            var mExpanded by remember { mutableStateOf(false) }

            // Create a string value to store the selected city
            var mSelectedText by remember { mutableStateOf("") }

            var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

            // Up Icon when expanded and down icon when collapsed
            val icon = if (mExpanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown

            Column(Modifier.padding(20.dp)) {
                // Create an Outlined Text Field
                // with icon and not expanded
                OutlinedTextField(
                    value = shopsViewModel.shopCategoriesSelectedItem.value,
                    onValueChange = { shopsViewModel.onShopCategoryUpdate(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            // This value is used to assign to
                            // the DropDown the same width
                            mTextFieldSize = coordinates.size.toSize()
                        },
                    label = { Text("Label") },
                    trailingIcon = {
                        Icon(icon, "contentDescription",
                            Modifier.clickable { mExpanded = !mExpanded })
                    }
                )

                // Create a drop-down menu with list of cities,
                // when clicked, set the Text Field text as the city selected
                DropdownMenu(
                    expanded = mExpanded,
                    onDismissRequest = { mExpanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
                ) {
                    shopsViewModel.shopCategories.value.forEach { label ->
                        DropdownMenuItem(onClick = {
                            mSelectedText = label
                            mExpanded = false
                        }, text = {
                            Text(text = label)
                        }
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Добавить карту",
                    fontFamily = fontMedium.toFontFamily(),
                    fontWeight = FontWeight.W500,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.background,
                )
            }
        }

    }
}