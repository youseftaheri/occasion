package com.iranwebyar.occasions.ui.occasionList.otherList

import com.iranwebyar.occasions.data.model.OccasionsPOJO

interface OtherListFragmentCallback {
    fun deleteCard(id: Long)
    fun editOccasion(occasion: OccasionsPOJO.Occasion)
}