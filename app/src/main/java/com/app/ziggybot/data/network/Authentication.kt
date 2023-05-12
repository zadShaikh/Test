package com.mycardsapplication.data.network

import com.app.ziggybot.data.network.model.parameterChat
import com.mycardsapplication.data.network.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface Authentication {

    @Multipart
    @POST("audio/transcriptions")
    fun speechToText(@HeaderMap headers: Map<String, String>,
                     @Part file1: MultipartBody.Part,
                     @Part file2: MultipartBody.Part,
                  ): Call<speechTotextResponse>



//    @FormUrlEncoded
    @POST("chat/completions")
    fun chatGptAPI(
    @HeaderMap headers: Map<String, String>,
    @Body paramChat: parameterChat
    ): Call<chatGptResponse>
}