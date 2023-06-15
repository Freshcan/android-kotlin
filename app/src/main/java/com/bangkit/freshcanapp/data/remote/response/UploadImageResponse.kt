package com.bangkit.freshcanapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadImageResponse(

    @field:SerializedName("status_code")
    val statusCode: Int? = null,

    @field:SerializedName("payload")
    val payload: PayloadUploadImage? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable

@Parcelize
data class PayloadUploadImage(
    @field:SerializedName("informationName")
    val informationName: String? = null,

    @field:SerializedName("id")
    val id: String? = null
): Parcelable