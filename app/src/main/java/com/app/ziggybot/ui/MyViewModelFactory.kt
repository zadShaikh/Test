package com.app.ziggybot.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.ziggybot.ui.Home.HomeViewModel
import com.mycardsapplication.data.network.ApiClient
import com.mycardsapplication.data.network.Authentication


class MyViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(ApiClient.getClient().create(Authentication::class.java)) as T

        }else {
            throw IllegalArgumentException("ViewModel Not Found")
        }

    }


}