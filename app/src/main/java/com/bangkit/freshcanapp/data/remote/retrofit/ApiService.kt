package com.bangkit.freshcanapp.data.remote.retrofit

import com.bangkit.freshcanapp.data.remote.request.LoginRequest
import com.bangkit.freshcanapp.data.remote.request.RegisterRequest
import com.bangkit.freshcanapp.data.remote.request.SpecificHistoryRequest
import com.bangkit.freshcanapp.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
//
    @POST("/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @GET("/gethistory")
    fun getAllDummyHistory(@Header("Authorization") token: String): Call<HistoryResponse>

    @GET("/getspecifichistory")
    fun getSpecificHistory(@Header("Authorization") token: String,
                            @Query("id") id: Int,
                            @Query("InformationName") informationName: String): Call<SpecificHistoryResponse>

    @Multipart
    @POST("/upload/image")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part("file") file: MultipartBody.Part
    ): Call<UploadImageResponse>
//
//    @GET("/v1/stories")
//    suspend fun getAllStories(@Header("Authorization") token: String,
//                      @Query("page") page: Int,
//                      @Query("size") size: Int): ListStoryResponse
//
//    @GET("/v1/stories")
//    fun getAllStoriesWLocation(@Header("Authorization") token: String,
//                                @Query("location") location: Int,
//                                @Query("size") size: Int): Call<ListStoryResponse>
//
//    @Multipart
//    @POST("/v1/stories")
//    fun uploadImage(
//        @Header("Authorization") token: String,
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody,
//    ): Call<FileUploadResponse>
//
//    @Multipart
//    @POST("/v1/stories")
//    fun uploadImageWLocation(
//        @Header("Authorization") token: String,
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody,
//        @Part("lat") lat: RequestBody,
//        @Part("lon") lon: RequestBody,
//    ): Call<FileUploadResponse>
}