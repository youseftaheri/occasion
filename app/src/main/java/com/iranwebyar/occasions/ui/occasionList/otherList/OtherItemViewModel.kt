package com.iranwebyar.occasions.ui.occasionList.otherList

import android.net.Uri
import androidx.databinding.ObservableField
import com.iranwebyar.occasions.R
import com.iranwebyar.occasions.data.model.OccasionsPOJO
import java.io.File
import java.util.*

class OtherItemViewModel(private val mOther: OccasionsPOJO.Occasion,
                         private val mListener: OtherItemViewModelListener) {

    init {
        mListener.setStatusColor("تایید شده")
        mListener.setCountdown()
    }

    @JvmField
    val text: ObservableField<String> = ObservableField(mOther.title)

    @JvmField
    val image: ObservableField<String> = ObservableField(
        when(mOther.image!!) {
            "تولد", "شخصی", "متفرقه" -> mOther.type!!
                else -> Uri.fromFile(File(mOther.image!!)).toString()
                }
        )

    @JvmField
    val id: ObservableField<Long?> = ObservableField(mOther.id)

    @JvmField
    val time: ObservableField<String?> = ObservableField(mOther.time)

    @JvmField
    val date: ObservableField<String?> = ObservableField(mOther.date)

    fun onEditClick() {
        mListener.onEditClick()
    }

    fun onDeleteClick() {
        mListener.onDeleteClick()
    }

    interface OtherItemViewModelListener {
        fun onEditClick()
        fun onDeleteClick()
        fun setStatusColor(status: String?)
        fun setCountdown()
    }
}
