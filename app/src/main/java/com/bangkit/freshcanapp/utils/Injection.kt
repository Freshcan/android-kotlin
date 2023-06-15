package com.bangkit.freshcanapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.bangkit.freshcanapp.data.FreshcanRepository
import com.bangkit.freshcanapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context, dataStore: DataStore<Preferences>): FreshcanRepository {
        val apiService = ApiConfig.getApiService()
//        val database = FreshcanDatabase.getDatabase(context)
        return FreshcanRepository.getInstance(/*database, */apiService, dataStore)
    }
}