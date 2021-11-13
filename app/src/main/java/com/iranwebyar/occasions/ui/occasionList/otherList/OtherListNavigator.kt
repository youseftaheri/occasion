package com.iranwebyar.occasions.ui.occasionList.otherList

import com.iranwebyar.occasions.data.model.db.Occasion

interface OtherListNavigator {
//    fun handleServerError(exception: String?)
//    fun showBlockedPage(message: String?)
    fun handleError(exception: String?)
    fun showLoading()
    fun hideLoading()
    fun setCards()
    fun successfulOccasionDelete()
    fun cancelAlarm(occasion: Occasion?)

}