package com.iranwebyar.occasions.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QuestionsPOJO : Serializable {
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
        @SerializedName("questions")
        var questions: List<Question>? = null
    }

    class Question : Serializable {
        @JvmField
        @Expose
        @SerializedName("id")
        var id: String? = null

        @JvmField
        @Expose
        @SerializedName("text")
        var text: String? = null

        @JvmField
        @Expose
        @SerializedName("image")
        var image: String? = null

        @JvmField
        @Expose
        @SerializedName("correctAnswerId")
        var correctAnswerId: String? = null

        @JvmField
        @Expose
        @SerializedName("answers")
        var answers: List<Answer>? = null
    }

    class Answer : Serializable {
        @JvmField
        @Expose
        @SerializedName("id")
        var id: String? = null

        @JvmField
        @Expose
        @SerializedName("text")
        var text: String? = null

        @JvmField
        @Expose
        @SerializedName("image")
        var image: String? = null
    }
}