package com.bangkit.freshcanapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryBodyResponse(
    @field:SerializedName("payload")
    var listHistory: List<HistoryItemResponse>? = null,

    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("status_code")
    var statusCode: String? = null,
): Parcelable

@Parcelize
data class HistoryItemResponse(
    @field:SerializedName("id")
    var id: String? = null,

    @field:SerializedName("createdDate")
    var tglDibuat: String? = null,

    @field:SerializedName("urlImage")
    var urlImage: String? = null,

    @field:SerializedName("informationName")
    var name: String? = null

): Parcelable