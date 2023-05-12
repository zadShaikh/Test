package com.mycardsapplication.common.utils

import android.text.TextUtils

object ValidationUtil {

    fun validateEmail(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false;
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    fun validatePassword(password: String): Boolean {
        return true;
    }
}