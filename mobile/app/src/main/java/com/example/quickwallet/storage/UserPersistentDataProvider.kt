package com.example.quickwallet.storage

import android.content.Context
import com.example.quickwallet.presentation.BaseApplication

class UserPersistentDataProvider(
    private val app: BaseApplication
): UserPersistentData {
    override fun save(data: String, storage: String, key: String) {
        app.applicationContext
            .getSharedPreferences(storage, Context.MODE_PRIVATE)
            .edit()
            .putString(key, data)
            .apply()
    }

    override fun getData(storage: String, key: String): String? {
        return app
            .applicationContext
            .getSharedPreferences(storage,Context.MODE_PRIVATE)
            .getString(key,null)
    }
}