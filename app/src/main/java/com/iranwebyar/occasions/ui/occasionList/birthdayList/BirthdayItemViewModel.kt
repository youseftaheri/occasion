package com.iranwebyar.occasions.ui.occasionList.birthdayList

import androidx.databinding.ObservableField
import com.iranwebyar.occasions.data.model.OccasionsPOJO

class BirthdayItemViewModel(private val mBirthday: OccasionsPOJO.Occasion,
                            private val mListener: BirthdayItemViewModelListener) {
    init {
        mListener.setStatusColor("تایید شده")
    }

    @JvmField
    val text: ObservableField<String> = ObservableField(mBirthday.title)

    @JvmField
    val image: ObservableField<String> = ObservableField(mBirthday.image)

    @JvmField
    val id: ObservableField<Long?> = ObservableField(mBirthday.id)

    fun onSelectClick() {
        mListener.onSelectClick()
    }

    fun onDeleteClick() {
        mListener.onDeleteClick()
    }

    interface BirthdayItemViewModelListener {
        fun onSelectClick()
        fun onDeleteClick()
        fun setStatusColor(status: String?)
    }
}
