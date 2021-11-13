package com.iranwebyar.occasions.ui.occasionList.personalList

import com.iranwebyar.occasions.data.model.OccasionsPOJO

interface PersonalListFragmentCallback {
    fun deleteCard(id: Long)
    fun editOccasion(occasion: OccasionsPOJO.Occasion)
}