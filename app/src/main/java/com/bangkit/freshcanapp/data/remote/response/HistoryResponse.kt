package com.bangkit.freshcanapp.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class HistoryResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("payload")
	val payload: List<PayloadItem>? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class PayloadItem(

	@field:SerializedName("condition")
	val condition: String? = null,

	@field:SerializedName("informationName")
	val informationName: String? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("percentage")
	val percentage: String? = null,

	@field:SerializedName("userEmail")
	val userEmail: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("urlImage")
	val urlImage: String? = null
) : Parcelable
