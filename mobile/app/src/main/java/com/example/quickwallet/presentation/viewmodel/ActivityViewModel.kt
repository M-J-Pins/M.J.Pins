package com.example.quickwallet.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.storage.UserPersistentData
import com.example.quickwallet.storage.UserPersistentDataProvider
import com.example.quickwallet.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel
@Inject constructor(
    private val app: BaseApplication,
    private val userPersistentData: UserPersistentData
) : ViewModel() {
    val accessToken: MutableState<String> = mutableStateOf("")
    val isFirstExecution: MutableState<Boolean> = mutableStateOf(true)
    init {
        fetchToken()
    }
    fun fetchToken() {
        viewModelScope.launch(Dispatchers.IO){
            val token = userPersistentData.getData(
                Constants.sharedPreferencesStorageName,
                Constants.sharedPreferencesTokenName
            )
            token?.let {
                accessToken.value = token
                isFirstExecution.value = false
            }
        }
    }
}