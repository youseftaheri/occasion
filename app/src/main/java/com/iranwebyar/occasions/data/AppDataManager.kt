package com.iranwebyar.occasions.data

import android.content.Context
import com.iranwebyar.occasions.data.local.prefs.PreferencesHelper
import com.iranwebyar.occasions.data.model.OccasionsPOJO
import com.iranwebyar.occasions.data.remote.ApiHelper
import com.google.gson.Gson
import com.iranwebyar.occasions.data.local.db.DbHelper
import com.iranwebyar.occasions.data.model.db.Occasion
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager
@Inject constructor(
    private val mContext: Context,
    private val mPreferencesHelper: PreferencesHelper,
    private val mApiHelper: ApiHelper,
    private val mDbHelper: DbHelper,
    private val mGson: Gson) : DataManager {

    companion object {
        private const val TAG = "AppDataManager"
    }

    override fun clear() {
        mPreferencesHelper.clear()
    }

    override var occasionsList: List<OccasionsPOJO.Occasion?>
        get() = mPreferencesHelper.occasionsList
        set(data) {
            mPreferencesHelper.occasionsList = data
        }

    override suspend fun deleteAll() {
        mDbHelper.deleteAll()
    }

    override suspend fun allOccasions(): List<Occasion>?{
        return mDbHelper.allOccasions()
    }

    override suspend fun insertOccasion(occasion: Occasion?) {
        mDbHelper.insertOccasion(occasion)
    }

    override suspend fun findOccasionById(id: Long?) {
        mDbHelper.findOccasionById(id)
    }

}