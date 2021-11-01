package com.iranwebyar.occasions.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iranwebyar.occasions.data.model.OccasionsPOJO
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
    fun addBirthdayItems(recyclerView: RecyclerView, birthdayItems: List<OccasionsPOJO.Occasion>?) {
        val adapter = BirthdayListAdapter(birthdayItems)
        recyclerView.adapter = adapter
    }
}