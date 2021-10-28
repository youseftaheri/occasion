package com.iranwebyar.occasions.ui.occasionList.birthdayList

import androidx.databinding.ObservableField
import com.iranwebyar.occasions.data.model.QuestionsPOJO

class BirthdayItemViewModel(private val mAnswer: QuestionsPOJO.Answer,
                            private val mListener: AnswersItemViewModelListener) {
    init {
        mListener.setStatusColor("تایید شده")
    }

    @JvmField
    val text: ObservableField<String> = ObservableField(mAnswer.text)

    @JvmField
    val image: ObservableField<String> = ObservableField(mAnswer.image)

    @JvmField
    val id: ObservableField<String?> = ObservableField(mAnswer.id)

    fun onSelectClick() {
        mListener.onSelectClick()
        mListener.onDeleteClick()
        mListener.onSelectClick()
    }

    interface AnswersItemViewModelListener {
        fun onSelectClick()
        fun onDeleteClick()
        fun setStatusColor(status: String?)
    }
}
