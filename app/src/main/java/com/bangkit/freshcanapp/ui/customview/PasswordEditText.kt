package com.bangkit.freshcanapp.ui.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

//    init {
//        // Set up an OnFocusChangeListener to check email format when the view loses focus
//        onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                checkEmailFormat()
//            }
//        }
//    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text?.length ?: 0 < 8) {
            // set error message
            setError("The password must have a minimum of 8 characters", null)
        } else {
            // clear error message
            error = null
        }
    }
}