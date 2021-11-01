package com.iranwebyar.occasions.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object Const {
    //    private const val MAIN_BASE = "https://salimiexchange.com/"
    private const val MAIN_BASE = "https://exchange.iranwebyar.com/"
    //    private const val MAIN_BASE = "http://192.168.217.238:8000/"
    var APK_FILE_URL = MAIN_BASE + "sitemedia/app/salimiexchange.apk"
    var PERFECT_MONEY_URL = MAIN_BASE + "api/app/perfectmoney/digital/currency/sell/"
    var INCREASE_WALLET_URL = MAIN_BASE + "api/app/user/increase/wallet/credit/"
    var PURCHASE_CURRENCY_URL = MAIN_BASE + "api/app/perfectmoney/digital/currency/sell/"
    var IMAGE_URL = MAIN_BASE + "api/app/show/picture/"
    var WEBSITE_RULES_URL = MAIN_BASE + "register/rulls/"
    var PAYMENT_URL = MAIN_BASE + "api/v1/"
    var BASE_URL = MAIN_BASE + "api/version/"
    const val DB_NAME = "sample_occasion_database.db"





    @JvmField
    var FONT_FILE = "Normal.ttf"
    var SELECTED_LANGUAGE = "selected_language_fjytghkjyghj67t"
    var LANGUAGE_ENGLISH = "en"
    var LANGUAGE_PERSIAN = "fa"
    @JvmField
    var ONE_HUNDRED = "100"
    @JvmField
    var SPLASH_TIME = 2000
    private const val USER_ID = "USER_ID"
    var SUCCESS = "1"
    @JvmField
    var ZERO = "0"
    @JvmField
    var ONE = "1"
    var TWO = "2"
    private const val AccountData = "AccountData"
    private const val LAST_APP_VERSION = "LAST_APP_VERSION"
    private const val IS_FORCE_UPDATE = "IS_FORCE_UPDATE"
    var MELLI_CODE = "MELLI_CODE"
    @JvmField
    var OTP_WAIT_TIME = 120000
    @JvmField
    var COUNTDOWN_INTERVAL = 1000
    var SCHOOL_ID = "SCHOOL_ID"
    var USER_TYPE = "USER_TYPE,jn"
    var SELECTED_SESSION = "SELECTED_SESSION"
    const val PREF_NAME = "utab_education_pref"
    @JvmField
    var CURRENY = "تومان"
    var IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch_gct5476uvt"
    var IS_UPDATE_SHOWN = "is_update_shown_dhrfsgdryrfbs"
    @JvmField
    var DEBTOR = "بدهکار"
    @JvmField
    var HOUR = "ساعت"
    @JvmField
    var MINUTE = "دقیقه"
    @JvmField
    var ST_FIN_COURSE = "ST_FIN_COURSE"
    @JvmField
    var ST_FIN_TEST = "ST_FIN_TEST"
    @JvmField
    var ST_FIN_INTERNET = "ST_FIN_INTERNET"
    @JvmField
    var ST_FIN_CLEARING = "ST_FIN_CLEARING"
    @JvmField
    var TE_FIN_COURSE = "TE_FIN_COURSE"
    @JvmField
    var TE_FIN_PAY = "TE_FIN_PAY"
    var checkingDescription1 = "درصورت عدم دریافت پیامک، کد "
    var checkingDescription2 = " را به شماره سامانه "
    var checkingDescription3 = " ارسال نمایید و سپس دکمه بررسی را بزنید."
    //____________________________________________________________________________________
    //__________________________________________________________________________________
    //<-------------------------------------------------------------------------------->\\
    //Get basic multipart api content
    //<-------------------------------------------------------------------------------->\\
    private fun getFileRequest(filePath: String): RequestBody {
        return File(filePath).toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        RequestBody.create(MediaType.parse("multipart/form-data"), File(filePath))
    }

    private fun getPlaneRequest(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
//        RequestBody.create(MediaType.parse("text/plain"), value)
    }

    private fun getFileKey(key: String, filePath: String): String {
        return "$key\";filename=\"$filePath"
    } //____________________________________________________________________________________________________
}