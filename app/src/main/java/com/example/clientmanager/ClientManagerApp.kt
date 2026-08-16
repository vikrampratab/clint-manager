package com.example.clientmanager

import android.app.Application
import com.example.clientmanager.data.AppDatabase

class ClientManagerApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
}
