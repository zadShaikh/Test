package com.mycardsapplication.base

import com.google.gson.annotations.SerializedName

open class BaseResponse {

    @SerializedName("status")
    val status: String? = null

    fun isSuccess() = "SUCCESS".equals(status, true)

    fun isError() = "ERROR".equals(status, true)

}