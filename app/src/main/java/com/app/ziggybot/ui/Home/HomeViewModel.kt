package com.app.ziggybot.ui.Home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.ziggybot.data.network.model.parameterChat
import com.mycardsapplication.base.BaseViewModel
import com.mycardsapplication.common.Result
import com.mycardsapplication.data.network.Authentication
import com.mycardsapplication.data.network.model.chatGptResponse
import com.mycardsapplication.data.network.model.speechTotextResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class HomeViewModel(
    private val authentication: Authentication
) : BaseViewModel() {

    val SpeechToTextApiResponseData = MutableLiveData<Result<speechTotextResponse>>()
    val ChatGptApiResponseData = MutableLiveData<Result<chatGptResponse>>()

    fun speechTotextApi(tokenStr: String,model:String,AudioPath:String) {
        val file = File(AudioPath)

        // Parsing any Media type file
        val requestBody1: RequestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
        val fileToUpload1: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody1)
        val id: MultipartBody.Part = MultipartBody.Part.createFormData("model", model)


        var headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer " + tokenStr
        // To perform task in background.
        SpeechToTextApiResponseData.postValue(Result.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            authentication.speechToText(headers,fileToUpload1,id)
                .enqueue(object : Callback<speechTotextResponse> {
                    override fun onResponse(
                        call: Call<speechTotextResponse>,
                        response: Response<speechTotextResponse>
                    ) {
                        if (response.isSuccessful) {
                            SpeechToTextApiResponseData.postValue(Result.Success(response.body()!!))
                        } else {
                            SpeechToTextApiResponseData.postValue(Result.Failure())
                        }
                    }

                    override fun onFailure(call: Call<speechTotextResponse>, t: Throwable) {
                        SpeechToTextApiResponseData.postValue(Result.Failure(throwable = t))
                    }
                })
        }
    }

//    fun ChatGptApi(tokenStr: String,model:String,jsonContetnt:String,chatGptResponseSub: ArrayList<messages>,msg:messages,obj : Json) {
    fun ChatGptApi(tokenStr: String,paramChat : parameterChat) {

        var headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer " + tokenStr
        headers["Content-Type"] = "application/json"



        // To perform task in background.
        ChatGptApiResponseData.postValue(Result.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            authentication.chatGptAPI(headers,paramChat)
                .enqueue(object : Callback<chatGptResponse> {
                    override fun onResponse(
                        call: Call<chatGptResponse>,
                        response: Response<chatGptResponse>
                    ) {
                        if (response.isSuccessful) {
                            ChatGptApiResponseData.postValue(Result.Success(response.body()!!))
                        } else {
                            ChatGptApiResponseData.postValue(Result.Failure())
                        }
                    }

                    override fun onFailure(call: Call<chatGptResponse>, t: Throwable) {
                        ChatGptApiResponseData.postValue(Result.Failure(throwable = t))
                    }
                })
        }
    }



}