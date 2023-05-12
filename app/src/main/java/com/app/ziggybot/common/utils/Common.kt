package com.mycardsapplication.common.utils

import android.app.Activity
import android.app.NotificationManager
import android.content.Context

import android.view.WindowManager

import androidx.navigation.NavOptions
import com.app.ziggybot.R

object Common {
    fun getScale(activity: Activity): Int {
        val display =
            (activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val width = display.width
        var valU = (width / 620).toDouble() //685
        valU = valU * 100.0 //100 tha old app me
        return valU.toInt()
    }





fun  getNavOptions() : NavOptions{

    val navOptions: NavOptions =
        NavOptions.Builder().setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right).build()
    return navOptions
}

    fun clearNotifications(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

}
