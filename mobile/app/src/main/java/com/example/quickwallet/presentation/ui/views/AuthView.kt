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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.example.quickwallet.R
import com.example.quickwallet.presentation.navigation.Screen
import com.example.quickwallet.presentation.viewmodel.ActivityViewModel
import com.example.quickwallet.presentation.viewmodel.AuthViewModel
import com.example.quickwallet.utils.PhoneNumberVisualTransformation

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
                .padding(16.dp, 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(320.dp, Alignment.Bottom)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Введите номер телефона",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    value = viewModel.phoneNumber.value,
                    visualTransformation = PhoneNumberVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = Color.Transparent,
                    ),
                    onValueChange = { viewModel.onPhoneNumberChanged(it) },
                    placeholder = {
                        Text(
                            "+ 7 000 000 00 00",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(true) }
                    )
                )
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(36.dp)
                    ),
                onClick = {
                    if (viewModel.phoneNumber.value.isBlank() ||
                        viewModel.phoneNumber.value.contains(" ")
                    ) {
                        focusManager.moveFocus(FocusDirection.Up)
                    } else {
                        viewModel.sendPhoneNumber()
                        navController.navigate(Screen.AuthScreens.AuthRequest.route)
                    }
                },
            ) {
                Text(
                    text = "Продолжить",
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
    activityViewModel: ActivityViewModel,
    navController: NavController
) {
    AppTheme {
        val focusManager = LocalFocusManager.current

        val keyboardController = LocalSoftwareKeyboardController.current
        keyboardController?.show()

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
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.Start)
                    .offset(x = (-10).dp),
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.Start),
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Back button"
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Введите код\nподтверждения",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Сейчас вам позвонят! Введите последние 4\nцифры номера, " +
                        "с которого поступил звонок",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(58.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CommonOtpTextField(
                    otp = viewModel.opt0.value,
                    index = 0,
                    onOptChange = viewModel::onCodeCellChange,
                    isCodeError = viewModel.isWrongCode.value,
                )
                CommonOtpTextField(
                    otp = viewModel.opt1.value,
                    index = 1,
                    onOptChange = viewModel::onCodeCellChange,
                    isCodeError = viewModel.isWrongCode.value,
                )
                CommonOtpTextField(
                    otp = viewModel.opt2.value,
                    index = 2,
                    onOptChange = viewModel::onCodeCellChange,
                    isCodeError = viewModel.isWrongCode.value,
                )
                CommonOtpTextField(
                    otp = viewModel.opt3.value,
                    index = 3,
                    onOptChange = viewModel::onCodeCellChange,
                    isCodeError = viewModel.isWrongCode.value,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    if (viewModel.isWrongCode.value) {
                        viewModel.sendPhoneNumber()
                    }
                },
            ) {
                ConditionalText(ticks = viewModel.backOrderTimerTicks.value)
            }

            checkFilledCells(
                areFilled = viewModel.isAllCodeCellsFilled.value,
                onSuccess = {
                    viewModel.sendPhoneAuth()
                    activityViewModel.fetchToken()
                }
            )

            checkCodeResult(
                isCodeError = viewModel.isWrongCode.value,
                responseReceived = viewModel.isTokenReceived.value,
                navController = navController
            )
        }
    }
}

@Composable
fun ConditionalText(ticks: Int) {
    if (ticks == 1) {
        Text(
            "Отправить повторно",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    } else {
        Text(
            "Отправить повторно через 0:${ticks}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun checkFilledCells(
    areFilled: Boolean,
    onSuccess: () -> Unit
) {
    if (areFilled) {
        onSuccess()

    }
}

@Composable
fun checkCodeResult(
    isCodeError: Boolean,
    responseReceived: Boolean,
    navController: NavController
) {
    if (!isCodeError && responseReceived) {
        navController.navigate(Screen.QuickWallet.QuickCards.route)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonOtpTextField(
    otp: String,
    index: Int,
    onOptChange: (cell: String, index: Int) -> Unit,
    isCodeError: Boolean
) {
    OutlinedTextField(
        value = otp,
        singleLine = true,
        onValueChange = {
            onOptChange(it, index)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .requiredSize(60.dp),
        maxLines = 1,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.W500
        ),
        isError = isCodeError
    )
}

@Composable
fun StartAuthScreen(
    navController: NavController
) {
    val focusManager = LocalFocusManager.current
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
            .padding(16.dp, 40.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .offset(y = 50.dp)
                .wrapContentSize()
        ) {
            Text(
                text = "Quick wallet",
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "быстрый поиск скидочных карт",
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = com.example.quickwallet.presentation.ui.theme.font.toFontFamily(),
                fontSize = 22.sp,
                fontWeight = FontWeight.W400,
            )
        }

        OutlinedButton(
            modifier = Modifier
                .offset(y = (-28).dp)
                .fillMaxWidth()
                .height(64.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(36.dp)
                ),
            onClick = {
                navController.navigate(Screen.AuthScreens.AuthPhoneRequest.route)
            }
        ) {
            Text(
                text = "Авторизация",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

}