package com.iranwebyar.occasions.data

import androidx.lifecycle.MediatorLiveData
import com.iranwebyar.occasions.data.local.db.DbHelper
import com.iranwebyar.occasions.data.local.prefs.PreferencesHelper
import com.iranwebyar.occasions.data.remote.ApiHelper

interface DataManager : DbHelper, PreferencesHelper, ApiHelper {

    companion object {
        val mData = MediatorLiveData<String>()
    }
}