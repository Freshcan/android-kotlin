package com.bangkit.freshcanapp.ui.customview;

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.util.regex.Pattern

class EmailEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }
    private val emailRegex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$"

    init {
        // Set up an OnFocusChangeListener to check email format when the view loses focus
        onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                checkEmailFormat()
            }
        }
    }

    private fun checkEmailFormat() {
        val email = text.toString()
        if (!isEmailValid(email)) {
            setError("Invalid email format", null)
            setTextColor(Color.RED)
        } else {
            setError(null)
            setTextColor(Color.BLACK)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Pattern.compile(emailRegex)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
}
