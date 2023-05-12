package com.mycardsapplication.data.network.model

import com.google.gson.annotations.SerializedName
import com.mycardsapplication.base.BaseResponse


data class chatGptResponse(

    @SerializedName("id")
    val id: String,

    @SerializedName("choices")
    val choices: ArrayList<chatGptResponseSub>


) : BaseResponse()

data class chatGptResponseSub(
    @SerializedName("message")
    val message: chatGptMessage
)

data class chatGptMessage(
    @SerializedName("role")
    val role: String,

    @SerializedName("content")
    val content: String
)

