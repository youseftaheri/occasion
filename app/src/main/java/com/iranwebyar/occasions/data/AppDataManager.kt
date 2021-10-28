package com.iranwebyar.occasions.data

import android.content.Context
import com.iranwebyar.occasions.data.local.prefs.PreferencesHelper
import com.iranwebyar.occasions.data.model.QuestionsPOJO
import com.iranwebyar.occasions.data.remote.ApiHelper
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager
@Inject constructor(
    private val mContext: Context,
    private val mPreferencesHelper: PreferencesHelper,
    private val mApiHelper: ApiHelper,
    private val mGson: Gson) : DataManager {

    companion object {
        private const val TAG = "AppDataManager"
    }

    override fun clear() {
        mPreferencesHelper.clear()
    }

    override var questionsData: QuestionsPOJO.Data?
        get() = mPreferencesHelper.questionsData
        set(data) {
            mPreferencesHelper.questionsData = data
        }

}