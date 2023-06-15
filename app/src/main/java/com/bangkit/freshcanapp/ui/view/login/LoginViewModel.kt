package com.bangkit.freshcanapp.ui.view.login

import android.util.Log
import androidx.lifecycle.*
import com.bangkit.freshcanapp.data.FreshcanRepository
import com.bangkit.freshcanapp.data.local.preferences.UserModel
import com.bangkit.freshcanapp.data.remote.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repo: FreshcanRepository) : ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<UserModel> {
        return repo.getUser().asLiveData()
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        repo.login(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {

                _isLoading.value = false
                if(response.isSuccessful){
                    val loginResponse = response.body()!!
                    _loginResponse.value = loginResponse
                    Log.e("LoginViewModel", "success")

                    viewModelScope.launch {
                        loginResponse.let { repo.saveUser(it) }
                    }
                }else if(response.code() == 401){
                    val loginResponse = LoginResponse("Invalid Email and Password")
                    _loginResponse.value = loginResponse

                }else{
                    val loginResponse = LoginResponse("Something went wrong")
                    _loginResponse.value = loginResponse
                }


            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val loginResponse = LoginResponse("Something went wrong")
                Log.e("test", "onFailure")
                _isLoading.value = false
                _loginResponse.value = loginResponse
            }
        })
    }
}