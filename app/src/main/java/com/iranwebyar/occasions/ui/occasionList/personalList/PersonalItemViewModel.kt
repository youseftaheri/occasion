package com.iranwebyar.occasions.ui.occasionList.personalList

import android.net.Uri
import androidx.databinding.ObservableField
import com.iranwebyar.occasions.R
import com.iranwebyar.occasions.data.model.OccasionsPOJO
import java.io.File
import java.util.*

class PersonalItemViewModel(private val mPersonal: OccasionsPOJO.Occasion,
                            private val mListener: PersonalItemViewModelListener) {

    init {
        mListener.setStatusColor("تایید شده")
        mListener.setCountdown()
    }

    @JvmField
    val text: ObservableField<String> = ObservableField(mPersonal.title)

    @JvmField
    val image: ObservableField<String> = ObservableField(
        when(mPersonal.image!!) {
            "تولد", "شخصی", "متفرقه" -> mPersonal.type!!
                else -> Uri.fromFile(File(mPersonal.image!!)).toString()
                }
        )

    @JvmField
    val id: ObservableField<Long?> = ObservableField(mPersonal.id)

    @JvmField
    val time: ObservableField<String?> = ObservableField(mPersonal.time)

    @JvmField
    val date: ObservableField<String?> = ObservableField(mPersonal.date)

    fun onEditClick() {
        mListener.onEditClick()
    }

    fun onDeleteClick() {
        mListener.onDeleteClick()
    }

    interface PersonalItemViewModelListener {
        fun onEditClick()
        fun onDeleteClick()
        fun setStatusColor(status: String?)
        fun setCountdown()
    }
}
