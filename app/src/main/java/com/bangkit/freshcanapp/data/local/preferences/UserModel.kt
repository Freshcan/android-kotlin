package com.bangkit.freshcanapp.data.local.preferences

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val email: String,
    val iat: String,
    val exp: String,
    val token: String,
    val isLogin: Boolean
): Parcelable