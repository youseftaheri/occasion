package com.iranwebyar.occasions.data.local.prefs

import com.iranwebyar.occasions.data.model.QuestionsPOJO

interface PreferencesHelper {
    fun clear()
    var questionsData: QuestionsPOJO.Data?
}