package com.iranwebyar.occasions.ui.newOccasion

interface NewOccasionNavigator {
//    fun handleServerError(exception: String?)
//    fun showBlockedPage(message: String?)
    fun back()
    fun handleError(exception: String?)
    fun showLoading()
    fun hideLoading()
    fun selectOccasion()
    fun onOccasionClick()
    fun onSubmitClick()
    fun onDateClick()
    fun onTimeClick()
    fun successfulOccasionAdd()
    fun pictureClick()
}