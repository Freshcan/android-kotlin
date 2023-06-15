package com.bangkit.freshcanapp.ui.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.freshcanapp.data.FreshcanRepository
import com.bangkit.freshcanapp.data.remote.response.RegisterResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val repo: FreshcanRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<List<Boolean>>()
    val isLoading: LiveData<List<Boolean>> = _isLoading


    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    fun register(name: String, email: String, password: String, confPassword: String){
        _isLoading.value = mutableListOf(true, false);
        repo.register(name, email, password, confPassword).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {

                if(response.isSuccessful){
                    _isLoading.value = mutableListOf(false, true)
                    _registerResponse.value = response.body()
                }else{
                    val errorBody = response.errorBody()?.string()
                    _registerResponse.value = Gson().fromJson(errorBody, RegisterResponse::class.java)
                    Log.e("signupmodel", "badrequest")
                    _isLoading.value = mutableListOf(false, false)
                }


            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("signupmodel", "error")
                _isLoading.value = mutableListOf(false, false)

            }

        })
    }
}