package com.app.ziggybot.data.network.model

import com.google.gson.annotations.SerializedName

data class parameterChat (
    @SerializedName("model")
    val model: String,

    @SerializedName("messages")
    val message1: ArrayList<message>
)

data class message (

    @SerializedName("role")
    val role: String,


    @SerializedName("content")
    val content: String

        )