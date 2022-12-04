package com.example.quickwallet.presentation.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.example.quickwallet.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AuthView(
    phone: String,
    codeCells: List<String>,
    onPhoneChanged: (String) -> Unit,
    onCodeCellChanged: (String, Int) -> Unit,
    isPhoneSendScreen: Boolean,
    isPhoneNumberError: Boolean,
    onPhoneSendScreen: (Boolean) -> Unit,
    sendPhoneNumber: () -> Unit,
    sendPhoneAuth: () -> Unit,
    isTokenReceived: Boolean,
    navController: NavController,
) {
    AppTheme {
        val focusManager = LocalFocusManager.current
        if (isPhoneNumberError) {
            Snackbar(
                action = {
                    Button(onClick = sendPhoneNumber) {
                        Text(
                            "Отправить",
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Что-то пошло не так. Отправить снова ?",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
        Image(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { focusManager.clearFocus(true) }
                )
                .fillMaxSize(),
            painter = painterResource(R.drawable.gradient),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (!isPhoneSendScreen) {
                Text(
                    modifier = Modifier.offset(y = 198.dp),
                    text = "Введите номер телефона",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                OutlinedTextField(
                    modifier = Modifier
                        .offset(y = 222.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .fillMaxWidth(),
                    value = phone,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = Color.Transparent,
                    ),
                    onValueChange = { onPhoneChanged(it) },
                    placeholder = {
                        Text(
                            "+ 7 000 000 00 00",
                            style = MaterialTheme.typography.titleMedium,
//                        color = Color(0x606C6C)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(true) })
                )
                OutlinedButton(
                    modifier = Modifier
                        .offset(y = 560.dp)
                        .height(73.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(36.dp)
                        )
                        .fillMaxWidth(),

                    onClick = sendPhoneNumber,
                ) {
                    Text(
                        "Продолжить",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } else {
                IconButton(
                    modifier = Modifier.offset(y = 20.dp),
                    onClick = { onPhoneSendScreen(true) }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIos,
                        contentDescription = "Back button"
                    )
                }
                Text(
                    modifier = Modifier
                        .offset(y = 36.dp),
                    text = "Введите код\nподтверждения",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    modifier = Modifier
                        .offset(y = 44.dp),
                    text = "Сейчас вам позвонят! Введите последние 4\nцифры номера, " +
                            "с которого поступил звонок",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    modifier = Modifier
                        .offset(y = 102.dp)
                        .fillMaxWidth(),
                ) {
                    CommonOtpTextField(
                        modifier = Modifier.offset(x = (-2).dp),
                        otp = codeCells[0],
                        index = 0,
                        onOptChange = onCodeCellChanged
                    )
                    CommonOtpTextField(
                        modifier = Modifier.offset(x = 34.dp),
                        otp = codeCells[1],
                        index = 1,
                        onOptChange = onCodeCellChanged
                    )
                    CommonOtpTextField(
                        modifier = Modifier.offset(x = 70.dp),
                        otp = codeCells[2],
                        index = 2,
                        onOptChange = onCodeCellChanged
                    )
                    CommonOtpTextField(
                        modifier = Modifier.offset(x = 106.dp),
                        otp = codeCells[3],
                        index = 3,
                        onOptChange = onCodeCellChanged
                    )
                }

            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonOtpTextField(
    modifier: Modifier,
    otp: String,
    index: Int,
    onOptChange: (cell: String, index: Int) -> Unit
) {
    val max = 1
    OutlinedTextField(
        value = otp,
        singleLine = true,
        onValueChange = {
            if (it.length <= max) {
                onOptChange(it, index)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .requiredSize(56.dp),

        maxLines = 1,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.W600,
        )
    )

}
