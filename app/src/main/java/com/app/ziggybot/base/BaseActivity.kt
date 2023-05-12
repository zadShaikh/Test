package com.mycardsapplication.base

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.app.ziggybot.R
import java.util.*

abstract class BaseActivity: AppCompatActivity(){
    val TAG = "BaseActivity"
    companion object {
//        public var dLocale: Locale? = null
    }
    val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navHostFragment.navController
    }
    init {
//        updateConfig(this)
    }
//    fun updateConfig(wrapper: BaseActivity) {
//        if(dLocale==Locale("") ) // Do nothing if dLocale is null
//            return
//
//        Locale.setDefault(dLocale)
//        val configuration = Configuration()
//        configuration.setLocale(dLocale)
//        wrapper.applyOverrideConfiguration(configuration)
//    }


}