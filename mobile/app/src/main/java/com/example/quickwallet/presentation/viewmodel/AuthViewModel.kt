package com.example.quickwallet.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.quickwallet.domain.model.AuthData
import com.example.quickwallet.domain.model.PhoneNumber
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.repository.AuthRepository
import com.example.quickwallet.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

private const val maxCodeCellLength = 1

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val repository: AuthRepository,
    private val app: BaseApplication
) : ViewModel() {

    val isTokenReceived: MutableState<Boolean> = mutableStateOf(false)

    val phoneNumber: MutableState<String> = mutableStateOf("")

    val opt0: MutableState<String> = mutableStateOf("")
    val opt1: MutableState<String> = mutableStateOf("")
    val opt2: MutableState<String> = mutableStateOf("")
    val opt3: MutableState<String> = mutableStateOf("")

    val isWrongCode: MutableState<Boolean> = mutableStateOf(false)
    val phoneNumberError: MutableState<Boolean> = mutableStateOf(false)

    val isAllCodeCellsFilled = mutableStateOf(false)

    val backOrderTimerTicks: MutableState<Int> = mutableStateOf(30)

    private fun startTimer() {
        viewModelScope.launch {
            backOrderTimerTicks.value = 30
            while (backOrderTimerTicks.value > 0) {
                delay(1.seconds)
                backOrderTimerTicks.value--
            }
        }
    }

    fun onPhoneNumberChanged(number: String) {
        this.phoneNumber.value = number
    }

    fun onCodeCellChange(code: String, index: Int) {
        if (code.length <= maxCodeCellLength
            && index < 4
        ) {
            when (index) {
                0 -> opt0.value = code
                1 -> opt1.value = code
                2 -> opt2.value = code
                3 -> opt3.value = code
            }
            if (opt0.value.isNotEmpty() &&
                opt1.value.isNotEmpty() &&
                opt2.value.isNotEmpty() &&
                opt3.value.isNotEmpty()
            ) {
                isAllCodeCellsFilled.value = true
            }
        }
    }

    fun sendPhoneNumber() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (repository.phoneAuthRequest(PhoneNumber(phoneNumber.value)) == null) {
                    phoneNumberError.value = true
                }
                startTimer()
            } catch (e: Exception) {
                Log.d(Constants.authViewModelLogTag, e.stackTraceToString())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun sendPhoneAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(Constants.authViewModelLogTag, "inside sendPhoneAuth()")
                repository.phoneAuth(
                    authData = AuthData(
                        phoneNumber = phoneNumber.value,
                        code = opt0.value + opt1.value + opt2.value + opt3.value
                    )
                )?.let{
                    Log.d(Constants.authViewModelLogTag, "Token: $it")
                    isTokenReceived.value = true
                }
            } catch (e: Exception) {
                Log.d(Constants.authViewModelLogTag, e.stackTraceToString())
            }
        }
    }
}