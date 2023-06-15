package com.bangkit.freshcanapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterResponse(
    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("token")
    var token: String? = null
): Parcelable
