package com.iranwebyar.occasions.ui.occasionList

interface OccasionListNavigator {
    fun handleError(exception: String?)
//    fun handleServerError(exception: String?)
//    fun showBlockedPage(message: String?)
    fun setUp()
    fun showLoading()
    fun hideLoading()
    fun onAddCardClick()
    fun openEditActivity()

}
