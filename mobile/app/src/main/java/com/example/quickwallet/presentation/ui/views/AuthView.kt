package com.example.quickwallet.presentation.ui.views

import android.widget.Toast
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.example.quickwallet.R
import com.example.quickwallet.presentation.viewmodel.AuthViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun SendPhoneNumberView(
    viewModel: AuthViewModel,
    navController: NavController,
) {
    AppTheme(useDarkTheme = false) {
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current
        if (viewModel.phoneNumberError.value) {
            Toast.makeText(context, "Что-то пошло не так. Попробуйте еще раз", Toast.LENGTH_LONG)
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
                value = viewModel.phoneNumber.value,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = Color.Transparent,
                ),
                onValueChange = { viewModel.onPhoneNumberChanged(it) },
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

                onClick = {
                    viewModel.sendPhoneNumber()
                    navController.navigate("send-code")
                },
            ) {
                Text(
                    "Продолжить",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SendCodeView(
    viewModel: AuthViewModel,
    navController: NavController
) {
    AppTheme {
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current
        if (viewModel.phoneNumberError.value) {
            Toast.makeText(context, "Что-то пошло не так. Попробуйте еще раз", Toast.LENGTH_LONG)
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
            IconButton(
                modifier = Modifier.offset(y = 20.dp),
                onClick = { navController.popBackStack() }
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
                val (item1, item2, item3, item4) = FocusRequester.createRefs()
                CommonOtpTextField(
                    modifier = Modifier
                        .offset(x = (-2).dp)
                        .focusRequester(item1)
                        .focusProperties {
                            next = item2
                            previous = item1
                        },
                    otp = viewModel.opt0.value,
                    index = 0,
                    onOptChange = viewModel::onCodeCellChange
                )
                CommonOtpTextField(
                    modifier = Modifier
                        .offset(x = 34.dp)
                        .focusRequester(item2)
                        .focusProperties {
                            next = item3
                            previous = item1
                        },
                    otp = viewModel.opt1.value,
                    index = 1,
                    onOptChange = viewModel::onCodeCellChange
                )
                CommonOtpTextField(
                    modifier = Modifier
                        .offset(x = 70.dp)
                        .focusRequester(item3)
                        .focusProperties {
                            next = item4
                            previous = item2
                        },
                    otp = viewModel.opt2.value,
                    index = 2,
                    onOptChange = viewModel::onCodeCellChange
                )
                CommonOtpTextField(
                    modifier = Modifier
                        .offset(x = 106.dp)
                        .focusRequester(item4)
                        .focusProperties {
                            next = item4
                            previous = item3
                        },
                    otp = viewModel.opt3.value,
                    index = 3,
                    onOptChange = viewModel::onCodeCellChange
                )
            }
            OutlinedButton(
                modifier = Modifier
                    .offset(y = 336.dp)
//                        .requiredWidth(236.dp)
                    .padding(16.dp, 12.dp),
//                        .fillMaxWidth(),

                onClick = {
                    viewModel.sendPhoneAuth()
                    navController.navigate("token")
                },
            ) {
                Text(
                    "Отправить повторно через 0:30",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
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
            onOptChange(it, index)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .requiredWidth(56.dp)
            .wrapContentSize(align = Alignment.Center),

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
