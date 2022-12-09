package com.example.quickwallet.presentation.ui.views.addcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import com.example.quickwallet.presentation.ui.theme.fontBold
import com.example.quickwallet.presentation.ui.theme.fontMedium


@Composable
@Preview
fun AddScreen(
) {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Упс...У вас пока нет карт",
                fontFamily = fontBold.toFontFamily(),
                fontWeight = FontWeight.W700,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary
                )
            Spacer(modifier = Modifier.height(8.dp))

            Box{
                Text(
                    text = "Давайте это исправим!",
                    fontFamily = fontMedium.toFontFamily(),
                    fontWeight = FontWeight.W500,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(horizontal = 120.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }
    }

}