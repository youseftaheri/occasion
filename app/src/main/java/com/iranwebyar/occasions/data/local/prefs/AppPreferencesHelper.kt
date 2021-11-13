package com.iranwebyar.occasions.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.iranwebyar.occasions.data.model.OccasionsPOJO
import com.iranwebyar.occasions.di.PreferenceInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
        private const val OCCASION_LIST = "OCCASION_LIST"
        private const val SELECTED_OCCASION = "SELECTED_OCCASION"


    }

    override fun clear() {
        mPrefs.edit().clear().apply()
    }

    init {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
    }

    //<-------------------------------------------------------------------------------->\\
    // Save and retrieve cities list
    //<-------------------------------------------------------------------------------->\\
    override var occasionsList: List<OccasionsPOJO.Occasion?>
        get() {
            var dataList: List<OccasionsPOJO.Occasion> = ArrayList()
            val strJson: String = mPrefs.getString(OCCASION_LIST, null) ?: return dataList
            val gson = Gson()
            dataList = gson.fromJson(strJson, object : TypeToken<List<OccasionsPOJO.Occasion>?>() {}.type)
            return dataList
        }
        //Gson().fromJson(mPrefs.getString(MY_CITIES, null), List<MainHomeFragPOJO.City>::class.java)
        set(data) {
            mPrefs.edit().putString(OCCASION_LIST, Gson().toJson(data)).apply()
        }

    //<------------------------------------------------------------------------------->\\
    //Store & retrieve selectedOccasion
    //<-------------------------------------------------------------------------------->\\
    override var selectedOccasion: OccasionsPOJO.Occasion?
        get() = Gson().fromJson(mPrefs.getString(SELECTED_OCCASION, null), OccasionsPOJO.Occasion::class.java)
        set(data) {
            mPrefs.edit().putString(SELECTED_OCCASION, Gson().toJson(data)).apply()
        }
}