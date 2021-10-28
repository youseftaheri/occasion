package com.iranwebyar.occasions.data.remote

import android.annotation.SuppressLint

@SuppressLint("NewApi")
class Events {
    class PermissionCheckerResult(var result: Boolean)
    class SmsResult(var otp: String)
    class NetworkStatus(var status: Boolean)
    class ImagePickerResult(var path: String)
}