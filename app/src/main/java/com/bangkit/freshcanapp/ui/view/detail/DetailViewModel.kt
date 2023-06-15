package com.bangkit.freshcanapp.ui.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.freshcanapp.data.FreshcanRepository
import com.bangkit.freshcanapp.data.remote.request.SpecificHistoryRequest
import com.bangkit.freshcanapp.data.remote.response.PayloadItem
import com.bangkit.freshcanapp.data.remote.response.SpecificHistoryResponse
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repo: FreshcanRepository) : ViewModel() {
    private val _specificHistoryResponse = MutableLiveData<SpecificHistoryResponse>()
    val specificHistoryResponse: LiveData<SpecificHistoryResponse> = _specificHistoryResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSpecificHistory(id: String, informationName: String) = runBlocking {
        val idInt = id.toInt()
        _isLoading.value = true
        repo.getSpecificHistory(idInt, informationName).enqueue(object: Callback<SpecificHistoryResponse> {
            override fun onResponse(
                call: Call<SpecificHistoryResponse>,
                response: Response<SpecificHistoryResponse>,
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _specificHistoryResponse.value = response.body()!!
                }else{
                    _specificHistoryResponse.value = SpecificHistoryResponse(500, null, "request failed")
                    Log.e("DetailViewModel", "status code failed")
                }
            }

            override fun onFailure(call: Call<SpecificHistoryResponse>, t: Throwable) {
                _isLoading.value = false
                _specificHistoryResponse.value = SpecificHistoryResponse(500, null, "not found")
                Log.e("DetailViewModel", t.message!!)
            }

        })
    }
}