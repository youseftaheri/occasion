package com.iranwebyar.occasions.data.remote

import android.content.Context
import com.iranwebyar.occasions.data.local.prefs.PreferencesHelper
//import com.example.quiz.data.model.*
import com.iranwebyar.occasions.utils.CommonUtils
//import com.example.quiz.utils.Const
//import io.reactivex.Single
//import okhttp3.RequestBody
//import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppApiHelper
@Inject constructor(
    private val apis: Apis,
    private val prefrenceHepler: PreferencesHelper,
    private val mContext: Context,
    private val commonUtils: CommonUtils
) : ApiHelper {

}
