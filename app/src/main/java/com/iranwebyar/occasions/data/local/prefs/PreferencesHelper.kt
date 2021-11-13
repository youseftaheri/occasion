package com.iranwebyar.occasions.data.local.prefs

import com.iranwebyar.occasions.data.model.OccasionsPOJO

interface PreferencesHelper {
    fun clear()
    var occasionsList: List<OccasionsPOJO.Occasion?>
    var selectedOccasion: OccasionsPOJO.Occasion?
}