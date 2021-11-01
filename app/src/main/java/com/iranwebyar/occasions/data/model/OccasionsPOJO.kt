package com.iranwebyar.occasions.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OccasionsPOJO : Serializable {
    @JvmField
    @Expose
    @SerializedName("status")
    var response: String? = null

    @JvmField
    @Expose
    @SerializedName("message")
    var message: String? = null

    @JvmField
    @Expose
    @SerializedName("data")
    var data: Data? = null

    inner class Data : Serializable {
        @JvmField
        @Expose
        @SerializedName("occasions")
        var occasions: List<Occasion>? = null
    }

    class Occasion : Serializable {
        @JvmField
        @Expose
        @SerializedName("id")
        var id: Long? = null

        @JvmField
        @Expose
        @SerializedName("type")
        var type: String? = null

        @JvmField
        @Expose
        @SerializedName("title")
        var title: String? = null

        @JvmField
        @Expose
        @SerializedName("image")
        var image: String? = null

        @JvmField
        @Expose
        @SerializedName("date")
        var date: String? = null

        @JvmField
        @Expose
        @SerializedName("time")
        var time: String? = null

        @JvmField
        @Expose
        @SerializedName("alarm")
        var alarm: Boolean? = null

        @JvmField
        @Expose
        @SerializedName("notification")
        var notification: Boolean? = null

    }
}