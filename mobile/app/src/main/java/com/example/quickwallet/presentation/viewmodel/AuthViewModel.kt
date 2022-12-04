package com.example.quickwallet.presentation.viewmodel

import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickwallet.domain.model.AuthData
import com.example.quickwallet.domain.model.PhoneNumber
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.repository.AuthRepository
import com.example.quickwallet.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val repository: AuthRepository,
    private val app: BaseApplication
) : ViewModel() {
    private val maxCodeCellLength = 1
    val phoneNumber: MutableState<String> = mutableStateOf("")
    val opt0:MutableState<String> = mutableStateOf("")
    val opt1:MutableState<String> = mutableStateOf("")
    val opt2:MutableState<String> = mutableStateOf("")
    val opt3:MutableState<String> = mutableStateOf("")
    val isPhoneSendScreen: MutableState<Boolean> = mutableStateOf(true)
    val isWrongCode: MutableState<Boolean> = mutableStateOf(false)
    val phoneNumberError: MutableState<Boolean> = mutableStateOf(false)
    val tokenReceived: MutableState<Boolean> = mutableStateOf(false)

    fun onPhoneNumberChanged(number: String) {
        this.phoneNumber.value = number
    }

    fun onCodeCellChange(code: String, index: Int) {
        if (code.length <= maxCodeCellLength
            && index < 4
        ) {
            when(index) {
                0->opt0.value=code
                1->opt1.value=code
                2->opt2.value=code
                3->opt3.value=code
            }
        }
    }

    fun sendPhoneNumber() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (repository.phoneAuthRequest(PhoneNumber(phoneNumber.value)) == null) {
                    phoneNumberError.value = true
                } else {
                    isPhoneSendScreen.value = false
                }
            } catch (e: Exception) {
                Log.d(Constants.authViewModelLogTag, e.stackTraceToString())
            }
        }
    }

    fun onPhoneSendScreen(state: Boolean) {
        isPhoneSendScreen.value = state
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun sendPhoneAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = async {
                    repository.phoneAuth(
                        AuthData(
                            phoneNumber = phoneNumber.value,
                            code = opt0.value+opt1.value+opt2.value+opt3.value
                        )
                    )
                }
                if (token.await() == null) {
                    isWrongCode.value = true
                } else {
                    Log.d(Constants.authViewModelLogTag, token.getCompleted()!!)
                    saveToken(token.getCompleted()!!)
                    tokenReceived.value = true
                }
            } catch (e: Exception) {
                Log.d(Constants.authViewModelLogTag, e.stackTraceToString())
            }
        }
    }

    private fun saveToken(token: String) {
        with(
            app.getSharedPreferences(Constants.sharedPreferencesStorageName, MODE_PRIVATE).edit()
        ) {
            putString(token, null)
            apply()
        }
    }

}