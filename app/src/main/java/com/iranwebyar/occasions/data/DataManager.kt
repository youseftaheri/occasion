package com.iranwebyar.occasions.data

import androidx.lifecycle.MediatorLiveData
import com.iranwebyar.occasions.data.local.prefs.PreferencesHelper
import com.iranwebyar.occasions.data.remote.ApiHelper

interface DataManager : PreferencesHelper, ApiHelper {

    companion object {
        val mData = MediatorLiveData<String>()
    }
}