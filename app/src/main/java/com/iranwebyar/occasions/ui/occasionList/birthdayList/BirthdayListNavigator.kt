package com.iranwebyar.occasions.ui.occasionList.birthdayList

interface BirthdayListNavigator {
//    fun handleServerError(exception: String?)
//    fun showBlockedPage(message: String?)
    fun handleError(exception: String?)
    fun showLoading()
    fun hideLoading()
//    fun setCards()
}