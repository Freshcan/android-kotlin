package com.bangkit.freshcanapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.auth0.android.jwt.JWT
import com.bangkit.freshcanapp.data.local.preferences.UserModel
import com.bangkit.freshcanapp.data.local.preferences.UserPreference
import com.bangkit.freshcanapp.data.remote.request.LoginRequest
import com.bangkit.freshcanapp.data.remote.request.RegisterRequest
import com.bangkit.freshcanapp.data.remote.request.SpecificHistoryRequest
import com.bangkit.freshcanapp.data.remote.response.*
import com.bangkit.freshcanapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call


class FreshcanRepository private constructor(
//    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val userPreference: UserPreference,
){
    fun getUser(): Flow<UserModel> {
        return userPreference.getUser();
    }

    suspend fun getAllDummyHistory(): Call<HistoryResponse> {
        return apiService.getAllDummyHistory("Bearer " + getUser().first().token)
    }

    suspend fun getSpecificHistory(id: Int, informationName: String): Call<SpecificHistoryResponse>{
        return apiService.getSpecificHistory("Bearer " + getUser().first().token,id,informationName)
    }

    suspend fun uploadImage(file: MultipartBody.Part): Call<UploadImageResponse>{
        return apiService.uploadImage(getUser().first().token, file)
    }

    fun register(name: String, email: String, password: String, confPassword: String): Call<RegisterResponse> {
        val resgisterRequest = RegisterRequest(name, email, password, confPassword)
        return apiService.register(resgisterRequest)
    }

    suspend fun saveUser(loginResponse: LoginResponse) {
        val token = loginResponse.token
        val jwt = JWT(token!!)

        val iat: String = jwt.getClaim("iat").asString()!!
        val exp: String = jwt.getClaim("exp").asString()!!
        val email: String = jwt.getClaim("email").asString()!!

        val user = UserModel(email, iat, exp, token, true)
        userPreference.saveUser(user)
    }

    suspend fun deleteUser() {
        userPreference.deleteUser()
    }

    fun login(name: String, password: String): Call<LoginResponse> {
        val loginRequest = LoginRequest(name, password)
        return apiService.login(loginRequest)
    }

    companion object {
        @Volatile
        private var instance: FreshcanRepository? = null
        fun getInstance(
//            database: StoryDatabase,
            apiService: ApiService,
            dataStore: DataStore<Preferences>,
        ): FreshcanRepository =
            instance ?: synchronized(this) {
                instance ?: FreshcanRepository(apiService, UserPreference.getInstance(dataStore))
            }.also { instance = it }
    }
}