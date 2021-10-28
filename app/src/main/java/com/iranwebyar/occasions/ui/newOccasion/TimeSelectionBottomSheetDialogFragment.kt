package com.iranwebyar.occasions.ui.newOccasion

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iranwebyar.occasions.R
import com.iranwebyar.occasions.utils.WheelView
import kotlinx.android.synthetic.main.fragment_time_selection_bottom_sheet_dialog.*
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DateSelectionBottomSheetDialogFragment  : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_time_selection_bottom_sheet_dialog,
            container,
            false
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dateData: MutableList<String> = ArrayList()
        val hourData: MutableList<String> = ArrayList()
        val minuteData: MutableList<String> = ArrayList()
        var mDate: String
        var mHour = "00"
        var mMinute = "00"
        var hour: String
        var minute: String

        val sdf = SimpleDateFormat("yyyy/MM/dd")

//        val endDate = Calendar.getInstance().time
        val endDate = getDaysAgo(-2)
        val startDate = getDaysAgo(30)

        val dates: MutableList<Date> = ArrayList()
        val calendar: Calendar = GregorianCalendar()
        calendar.time = startDate

        while (calendar.time.before(endDate)) {
            val result = calendar.time
            dates.add(result)
            calendar.add(Calendar.DATE, 1)
        }

        hourData.clear()
        for (i in 0..24) {
            hour = i.toString() + ""
            if (i < 10) {
                hour = "0$hour"
            }
            hourData.add(hour)
        }
        wvHour.setData(hourData)
        wvHour.setSelectIndex(0)
        wvHour.setOnSelectedListener(object : WheelView.OnScrollListener {
            override fun selected(data: String?) {
                mHour = data!!
            }
        })

        minuteData.clear()
        for (i in 0..60) {
            minute = i.toString() + ""
            if (i < 10) {
                minute = "0$minute"
            }
            minuteData.add(minute)
        }
        wvMinute.setData(minuteData)
        wvMinute.setSelectIndex(0)
        wvMinute.setOnSelectedListener(object : WheelView.OnScrollListener {
            override fun selected(data: String?) {
                mMinute = data!!
            }
        })

        firstButton.setOnClickListener {
            (activity as NewOccasionActivity?)!!.selectTime(
                mHour, mMinute)
            dismiss()
        }
    }

    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)

        return calendar.time
    }
    var swipeEnabled = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog
                .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            if (bottomSheet != null) {
                val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
                behavior.isDraggable = false
            }
        }
        return bottomSheetDialog
    }
}