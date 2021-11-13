package com.iranwebyar.occasions.ui.occasionList.birthdayList

import android.net.Uri
import androidx.databinding.ObservableField
import com.iranwebyar.occasions.R
import com.iranwebyar.occasions.data.model.OccasionsPOJO
import java.io.File
import java.util.*

class BirthdayItemViewModel(private val mBirthday: OccasionsPOJO.Occasion,
                            private val mListener: BirthdayItemViewModelListener) {

    init {
        mListener.setStatusColor("تایید شده")
        mListener.setCountdown()
    }

    @JvmField
    val text: ObservableField<String> = ObservableField(mBirthday.title)

    @JvmField
    val image: ObservableField<String> = ObservableField(
        when(mBirthday.image!!) {
            "تولد", "شخصی", "متفرقه" -> mBirthday.type!!
                else -> Uri.fromFile(File(mBirthday.image!!)).toString()
                }
        )

    @JvmField
    val id: ObservableField<Long?> = ObservableField(mBirthday.id)

    @JvmField
    val time: ObservableField<String?> = ObservableField(mBirthday.time)

    @JvmField
    val date: ObservableField<String?> = ObservableField(mBirthday.date)

    fun onEditClick() {
        mListener.onEditClick()
    }

    fun onDeleteClick() {
        mListener.onDeleteClick()
    }

    interface BirthdayItemViewModelListener {
        fun onEditClick()
        fun onDeleteClick()
        fun setStatusColor(status: String?)
        fun setCountdown()
    }
}
