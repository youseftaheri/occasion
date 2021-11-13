package com.iranwebyar.occasions.utils

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.helper.widget.MotionPlaceholder
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iranwebyar.occasions.R
import com.iranwebyar.occasions.data.model.OccasionsPOJO
import com.iranwebyar.occasions.ui.occasionList.birthdayList.BirthdayListAdapter
import com.iranwebyar.occasions.utils.CommonUtils

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

//    @JvmStatic
//    @BindingAdapter("birthdayListAdapter")
//    fun addBirthdayItems(recyclerView: RecyclerView, birthdayItems: List<OccasionsPOJO.Occasion>?) {
//        val adapter = BirthdayListAdapter(birthdayItems)
//        recyclerView.adapter = adapter
//    }

    @JvmStatic
    @BindingAdapter("android:imageUrl")
    fun setImageUrl(imageView: ImageView, url: String?) {
        val context = imageView.context
        Glide.with(context).load(url).dontAnimate().into(imageView)

    }

    @JvmStatic
    @BindingAdapter("android:imageUri")
    fun setImageUri(imageView: ImageView, uriString: String?) {
        val context = imageView.context
        when(uriString){
            "تولد" -> imageView.setImageResource(R.drawable.birthday)
            "شخصی" -> imageView.setImageResource(R.drawable.personal)
            "متفرقه" -> imageView.setImageResource(R.drawable.others)
            else -> CommonUtils.loadImageWithoutCash(
                context,
                uriString,
                imageView,
                R.drawable.ic_image_pick
            )
        }

    }
}