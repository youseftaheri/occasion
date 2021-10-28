package com.iranwebyar.occasions.ui.imagePicker

import android.content.Intent

interface ImagePickerNavigator {
//    fun handleServerError(exception: String?)
//    fun showBlockedPage(message: String?)
//    fun handleError(exception: String?)
    fun showLoading()
    fun hideLoading()
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
}