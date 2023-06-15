package com.bangkit.freshcanapp.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SpecificHistoryResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("payload")
	val payload: Payload? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class QueryHistoryItem(

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

@Parcelize
data class Payload(

	@field:SerializedName("queryHistory")
	val queryHistory: List<QueryHistoryItem?>? = null,

	@field:SerializedName("queryInformation")
	val queryInformation: List<QueryInformationItem?>? = null
) : Parcelable

@Parcelize
data class QueryInformationItem(

	@field:SerializedName("carbohydrates")
	val carbohydrates: String? = null,

	@field:SerializedName("total_fat")
	val totalFat: String? = null,

	@field:SerializedName("fiber")
	val fiber: String? = null,

	@field:SerializedName("sugars")
	val sugars: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("allergy")
	val allergy: String? = null,

	@field:SerializedName("calsium")
	val calsium: String? = null,

	@field:SerializedName("funfact")
	val funfact: String? = null,

	@field:SerializedName("water")
	val water: String? = null,

	@field:SerializedName("benefit")
	val benefit: String? = null,

	@field:SerializedName("protein")
	val protein: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("botanical_name")
	val botanicalName: String? = null,

	@field:SerializedName("iron")
	val iron: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("energy")
	val energy: String? = null
) : Parcelable
