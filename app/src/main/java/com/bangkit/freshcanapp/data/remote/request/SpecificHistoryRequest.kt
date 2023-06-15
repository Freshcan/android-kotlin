package com.bangkit.freshcanapp.data.remote.request

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SpecificHistoryRequest(

	@field:SerializedName("informationName")
	val informationName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
) : Parcelable
