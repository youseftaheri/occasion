package com.iranwebyar.occasions.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iranwebyar.occasions.data.model.QuestionsPOJO
import com.iranwebyar.occasions.ui.occasionList.birthdayList.BirthdayListAdapter

object BindingUtils {
    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, value: Boolean) {
        view.visibility = if(value) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, value: Int) {
        view.visibility = if(value == 1) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("birthdayListAdapter")
    fun addBirthdayItems(recyclerView: RecyclerView, answersItems: List<QuestionsPOJO.Answer?>?) {
        val adapter = BirthdayListAdapter(answersItems)
        recyclerView.adapter = adapter
    }
}