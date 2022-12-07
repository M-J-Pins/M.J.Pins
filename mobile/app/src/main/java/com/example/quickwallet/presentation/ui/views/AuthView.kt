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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.example.quickwallet.R
import com.example.quickwallet.presentation.viewmodel.AuthViewModel
import com.example.quickwallet.utils.PhoneNumberVisualTransformation
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

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
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
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
            val leftPadding = createGuidelineFromAbsoluteLeft(0.0444f)
            val rightPadding = createGuidelineFromAbsoluteRight(0.0444f)
            val sendTextTop = createGuidelineFromTop(0.2675f)
            val sendTextBottom = createGuidelineFromBottom(0.69f)
            val textFieldBottom = createGuidelineFromBottom(0.59f)
            val (enterPhoneText, textField, button, btnText) = createRefs()
            Text(
                modifier = Modifier
                    .constrainAs(enterPhoneText) {
                        linkTo(leftPadding, rightPadding)
                        linkTo(sendTextTop, sendTextBottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(),
                text = "Введите номер телефона",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            OutlinedTextField(
                modifier = Modifier
                    .constrainAs(textField) {
                        linkTo(leftPadding, rightPadding)
                        width = Dimension.fillToConstraints
                        top.linkTo(enterPhoneText.bottom, 24.dp)
                        bottom.linkTo(textFieldBottom)
                        height = Dimension.fillToConstraints
                    }
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
            val buttonBottom = createGuidelineFromBottom(0.109f)
            val buttonTop = createGuidelineFromTop(0.811f)
            OutlinedButton(
                modifier = Modifier
                    .constrainAs(button) {
                        linkTo(leftPadding, rightPadding)
                        linkTo(buttonTop, buttonBottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
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
                        viewModel.startTimer()
                        navController.navigate("send-code")
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
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (iconButton, sendCodeMainText, helperText, sendAgainButton) = createRefs()

            val enterCodeTextTop = createGuidelineFromTop(0.125f)
            val enterCodeTextBottom = createGuidelineFromBottom(0.79f)

            val leftPadding16 = createGuidelineFromAbsoluteLeft(0.0444f)
            val rightPadding16 = createGuidelineFromAbsoluteRight(0.0444f)
            val backBtnTop = createGuidelineFromTop(0.075f)
            IconButton(
                modifier = Modifier.constrainAs(iconButton) {
                    start.linkTo(leftPadding16,(-2).dp)
                    top.linkTo(backBtnTop)
                },
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Back button"
                )
            }
            Text(
                modifier = Modifier
                    .constrainAs(sendCodeMainText) {
                        linkTo(enterCodeTextTop, enterCodeTextBottom)
                        height = Dimension.wrapContent
                        linkTo(leftPadding16, rightPadding16, 1.dp)
                        width = Dimension.fillToConstraints
                    },
                text = "Введите код\nподтверждения",
                style = MaterialTheme.typography.headlineMedium
            )
            val helperTextBottom = createGuidelineFromBottom(0.7325f)
            Text(
                modifier = Modifier
                    .constrainAs(helperText) {
                        linkTo(leftPadding16, rightPadding16)
                        width = Dimension.fillToConstraints
                        top.linkTo(sendCodeMainText.bottom, 8.dp)
                        bottom.linkTo(helperTextBottom)
                        height = Dimension.wrapContent
                    },
                text = "Сейчас вам позвонят! Введите последние 4\nцифры номера, " +
                        "с которого поступил звонок",
                style = MaterialTheme.typography.bodyLarge
            )
            val codeCellTop = createGuidelineFromTop(0.34f)
            val codeCellBottom = createGuidelineFromBottom(0.59f)
            val (cell0, cell1, cell2, cell3) = createRefs()
            val leftCell = createGuidelineFromAbsoluteLeft(0.0389f)
            CommonOtpTextField(
                modifier = Modifier
                    .constrainAs(cell0) {
                        linkTo(helperText.bottom, codeCellBottom)
                        height = Dimension.fillToConstraints
                        start.linkTo(leftCell)
                        width = Dimension.ratio("1:1")
                    },
                otp = viewModel.opt0.value,
                index = 0,
                onOptChange = viewModel::onCodeCellChange,
                isCodeError = viewModel.isWrongCode.value,
            )
            CommonOtpTextField(
                modifier = Modifier
                    .constrainAs(cell1) {
                        linkTo(helperText.bottom, codeCellBottom)
                        height = Dimension.fillToConstraints
                        start.linkTo(cell0.end, 32.dp)
                        width = Dimension.ratio("1:1")
                    },
                otp = viewModel.opt1.value,
                index = 1,
                onOptChange = viewModel::onCodeCellChange,
                isCodeError = viewModel.isWrongCode.value,
            )
            CommonOtpTextField(
                modifier = Modifier
                    .constrainAs(cell2) {
                        linkTo(helperText.bottom, codeCellBottom)
                        height = Dimension.fillToConstraints
                        start.linkTo(cell1.end, 32.dp)
                        width = Dimension.ratio("1:1")
                    },
                otp = viewModel.opt2.value,
                index = 2,
                onOptChange = viewModel::onCodeCellChange,
                isCodeError = viewModel.isWrongCode.value,
            )
            CommonOtpTextField(
                modifier = Modifier
                    .constrainAs(cell3) {
                        linkTo(helperText.bottom, codeCellBottom)
                        height = Dimension.fillToConstraints
                        start.linkTo(cell2.end, 32.dp)
                        width = Dimension.ratio("1:1")
                    },
                otp = viewModel.opt3.value,
                index = 3,
                onOptChange = viewModel::onCodeCellChange,
                isCodeError = viewModel.isWrongCode.value,
            )

            checkFilledCells(
                areFilled = viewModel.isAllCodeCellsFilled.value,
                onSuccess = viewModel::sendPhoneAuth
            )

            checkCodeResult(
                viewModel.isWrongCode.value,
                viewModel.isTokenReceived.value,
                navController
            )

            val sendAgainButtonTop = createGuidelineFromTop(0.44f)
            val sendAgainButtonBottom = createGuidelineFromBottom(0.509f)

            OutlinedButton(
                modifier = Modifier
                    .constrainAs(sendAgainButton) {
                        end.linkTo(cell3.end)
                        linkTo(sendAgainButtonTop, sendAgainButtonBottom)
                        height = Dimension.fillToConstraints
                    },

                onClick = {
                    if (viewModel.isWrongCode.value) {

                        viewModel.sendPhoneNumber()
                    }
                },
            ) {
                ConditionalText(ticks = viewModel.backOrderTimerTicks.value)
            }
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
        navController.navigate("my-cards")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonOtpTextField(
    modifier: Modifier,
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
        ),
        isError = isCodeError
    )

}
