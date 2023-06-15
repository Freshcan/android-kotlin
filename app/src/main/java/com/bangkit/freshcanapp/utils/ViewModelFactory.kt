package com.bangkit.freshcanapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.freshcanapp.data.FreshcanRepository
import com.bangkit.freshcanapp.ui.view.detail.DetailViewModel
import com.bangkit.freshcanapp.ui.view.history.HistoryViewModel
import com.bangkit.freshcanapp.ui.view.login.LoginViewModel
import com.bangkit.freshcanapp.ui.view.main.MainViewModel
import com.bangkit.freshcanapp.ui.view.register.RegisterViewModel

class ViewModelFactory(private val repo: FreshcanRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repo) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repo) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repo) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repo) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}