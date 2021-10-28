package com.iranwebyar.occasions.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {
    /**
     * Check internet availabilty
     *
     * @param context Context of activity or fragment
     * @return Returns true is internet connected and false if no internet connected
     */
    @JvmStatic
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
        return false
    }
}