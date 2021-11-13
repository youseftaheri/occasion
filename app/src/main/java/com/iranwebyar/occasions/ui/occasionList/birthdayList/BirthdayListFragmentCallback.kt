package com.iranwebyar.occasions.ui.occasionList.birthdayList

import com.iranwebyar.occasions.data.model.OccasionsPOJO

interface BirthdayListFragmentCallback {
    fun deleteCard(id: Long)
    fun editOccasion(occasion: OccasionsPOJO.Occasion)
}