package com.iranwebyar.occasions.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.iranwebyar.occasions.data.model.QuestionsPOJO
import com.iranwebyar.occasions.di.PreferenceInfo
import com.google.gson.Gson
import javax.inject.Inject

class AppPreferencesHelper @Inject constructor(
    context: Context,
    @PreferenceInfo prefFileName: String?
) : PreferencesHelper {
    private val mPrefs: SharedPreferences

    companion object {
        private const val PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE"
        private const val PAYMENT_URL = "https://.../"
        private const val MAIN_BASE = "http://selimi.ir/portal/"
        var BASE_URL = MAIN_BASE + "api-v1/"
        var IMAGE_BASE = "http://.../portal/assets/profile-picture/"
        private const val QUESTIONS_DATA = "QUESTIONS_DATA"


    }

    override fun clear() {
        mPrefs.edit().clear().apply()
    }

    init {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
    }

    //<------------------------------------------------------------------------------->\\
    //Store & retrieve questionsData
    //<-------------------------------------------------------------------------------->\\
    override var questionsData: QuestionsPOJO.Data?
        get() = Gson().fromJson(mPrefs.getString(QUESTIONS_DATA, null), QuestionsPOJO.Data::class.java)
        set(data) {
            mPrefs.edit().putString(QUESTIONS_DATA, Gson().toJson(data)).apply()
        }
//    override var questionsData: QuestionsPOJO.Data?
//        get() = Gson().fromJson(mPrefs.getString(QUESTIONS_DATA, null), QuestionsPOJO.Data::class.java)
//        set(data) {
//            mPrefs.edit().putString(QUESTIONS_DATA, Gson().toJson(data)).apply()
//        }
}