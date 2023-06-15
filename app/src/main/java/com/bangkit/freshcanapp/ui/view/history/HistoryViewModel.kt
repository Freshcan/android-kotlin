package com.bangkit.freshcanapp.ui.view.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.freshcanapp.data.FreshcanRepository
import com.bangkit.freshcanapp.data.remote.response.HistoryBodyResponse
import com.bangkit.freshcanapp.data.remote.response.HistoryItemResponse
import com.bangkit.freshcanapp.data.remote.response.HistoryResponse
import com.bangkit.freshcanapp.data.remote.response.PayloadItem
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel(private val repo: FreshcanRepository) : ViewModel() {
    private val _listHistoryResponse = MutableLiveData<List<PayloadItem>>()
    val listHistoryResponse: LiveData<List<PayloadItem>> = _listHistoryResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllDummyHistory() = runBlocking {
        _isLoading.value = true
        repo.getAllDummyHistory().enqueue(object: Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>,
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _listHistoryResponse.value = response.body()?.payload!!
                }else{
                    Log.e("HistoryViewModel", "status code failed")
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                Log.e("HistoryViewModel", t.message!!)
            }

        })
    }
}