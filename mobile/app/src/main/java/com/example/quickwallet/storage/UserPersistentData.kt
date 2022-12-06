package com.example.quickwallet.storage

interface UserPersistentData {
    fun save(data:String,storage: String, key: String)
    fun getData(storage: String, key: String): String?
}