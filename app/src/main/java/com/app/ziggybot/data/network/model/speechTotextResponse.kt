package com.mycardsapplication.data.network.model

import com.google.gson.annotations.SerializedName
import com.mycardsapplication.base.BaseResponse


data class speechTotextResponse(

    @SerializedName("text")
    val textt: String


): BaseResponse()
