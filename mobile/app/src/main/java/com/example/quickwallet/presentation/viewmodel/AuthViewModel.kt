package com.example.quickwallet.presentation.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickwallet.domain.model.AuthData
import com.example.quickwallet.domain.model.PhoneNumber
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.repository.AuthRepository
import com.example.quickwallet.utils.Constants
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
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

    val phoneNumber: MutableState<String> = mutableStateOf(Constants.countryPhoneNumberCode)
    val code: MutableState<String> = mutableStateOf("")
    val isPhoneNumberSent: MutableState<Boolean> = mutableStateOf(false)
    val isWrongCode: MutableState<Boolean> = mutableStateOf(false)
    val phoneNumberError: MutableState<Boolean> = mutableStateOf(false)
    val tokenReceived: MutableState<Boolean> = mutableStateOf(false)

    fun onPhoneNumberChanged(number: String) {
        if (number.startsWith(Constants.countryPhoneNumberCode)) {
            this.phoneNumber.value = number
        }
    }

    fun onCodeChanged(code: String) {
        this.code.value = code
    }


    fun sendPhoneNumber() {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                if (repository.phoneAuthRequest(PhoneNumber(phoneNumber.value)) == null) {
                    phoneNumberError.value = true
                } else {
                    isPhoneNumberSent.value = true
                }
            } catch (e: Exception) {
                Log.d(Constants.authViewModelLogTag, e.stackTraceToString())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun sendPhoneAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = async{repository.phoneAuth(
                    AuthData(
                        phoneNumber = phoneNumber.value,
                        code = code.value
                    )
                )}
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