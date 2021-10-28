package com.iranwebyar.occasions.ui.newOccasion

interface NewOccasionNavigator {
//    fun handleServerError(exception: String?)
//    fun showBlockedPage(message: String?)
    fun back()
//    fun handleError(exception: String?)
    fun showLoading()
    fun hideLoading()
    fun selectOccasion()
    fun onOccasionClick()
    fun onSubmitClick()
    fun successfulOccasionAdd(exception: String?)
    fun pictureClick()
}