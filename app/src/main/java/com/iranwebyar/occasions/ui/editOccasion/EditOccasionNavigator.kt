package com.iranwebyar.occasions.ui.editOccasion

import com.iranwebyar.occasions.data.model.OccasionsPOJO
import com.iranwebyar.occasions.data.model.db.Occasion

interface EditOccasionNavigator {
    //    fun handleServerError(exception: String?)
//    fun showBlockedPage(message: String?)
    fun back()
    fun handleError(exception: String?)
    fun showLoading()
    fun hideLoading()
    fun setData(selectedOccasion: OccasionsPOJO.Occasion?)
    fun selectOccasion()
    fun onOccasionClick()
    fun onSubmitClick()
    fun onDateClick()
    fun onTimeClick()
    fun successfulOccasionAdd(occasion: Occasion?)
    fun pictureClick()
}