package com.iranwebyar.occasions.ui.occasionList.personalList

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iranwebyar.occasions.data.model.OccasionsPOJO
import com.iranwebyar.occasions.databinding.ItemPersonalViewBinding
import com.iranwebyar.occasions.ui.base.BaseAdapter
import com.iranwebyar.occasions.ui.base.BaseViewHolder
import java.util.*
import saman.zamani.persiandate.PersianDate


class PersonalListAdapter
constructor(personalItemViewModelList: List<OccasionsPOJO.Occasion?>?, callback: PersonalListFragmentCallback) : BaseAdapter<OccasionsPOJO.Occasion?>() {
    var callback: PersonalListFragmentCallback? = callback
    var publicParent: ViewGroup? = null

    override fun onBindViewHolderBase(holder: BaseViewHolder?, position: Int) {
        holder!!.onBind(position)
    }

    override fun onCreateViewHolderBase(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        publicParent = parent
        val mPersonalViewBinding = ItemPersonalViewBinding
            .inflate(LayoutInflater.from(parent!!.context), parent, false)
        return PersonalViewHolder(mPersonalViewBinding)
    }

    inner class PersonalViewHolder(var mBinding: ItemPersonalViewBinding) : BaseViewHolder(mBinding.root),
        PersonalItemViewModel.PersonalItemViewModelListener {
        private var personalItemViewModel: PersonalItemViewModel? = null
        private val handler = Handler()

        override fun onBind(position: Int) {
            val session = dataList!![position]!!
            personalItemViewModel = PersonalItemViewModel(session, this)
            mBinding.viewModel = personalItemViewModel
            mBinding.executePendingBindings()
        }
        override fun onDeleteClick() {
            dataList!![layoutPosition]!!.id?.let {
                callback!!.deleteCard(it)
            }
        }
        override fun setStatusColor(status: String?) {
//            mBinding.cvLabel.setCardBackgroundColor(
//                if(status == "تایید شده")
//                    Color.parseColor("#73FF94")
//                else
//                    Color.parseColor("#FD9C95"))
        }

        override fun onEditClick() {
            dataList!![layoutPosition]?.let {
                callback!!.editOccasion(it)
            }
        }

        override fun setCountdown() {
            handler.post(object : Runnable {
                override fun run() {
                    // Keep the postDelayed before the updateTime(), so when the event ends, the handler will stop too.
                    handler.postDelayed(this, 1000)
                    if(layoutPosition in dataList!!.indices)
                        updateTime()
                }
            })
        }
        fun updateTime() {

            val pDate = PersianDate()
            pDate.shYear = dataList!![layoutPosition]!!.date!!.split("/")[0].toInt()
            pDate.shMonth = dataList!![layoutPosition]!!.date!!.split("/")[1].toInt()
            pDate.shDay = dataList!![layoutPosition]!!.date!!.split("/")[2].toInt()
            pDate.hour = dataList!![layoutPosition]!!.time!!.split(":")[0].toInt()
            pDate.minute = dataList!![layoutPosition]!!.time!!.split(":")[1].toInt()
            pDate.second = 0

            val convertedDate = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"))
            if(dataList!![layoutPosition]!!.time!!.split(":")[0].toInt() == 12)
                convertedDate.time.time = pDate.toDate().time
            else
                convertedDate.time = pDate.toDate()

            // Set Current Date
            val currentDate = Calendar.getInstance()

            // Set Event Date
            val eventDate = Calendar.getInstance()
            eventDate[Calendar.YEAR] = convertedDate[Calendar.YEAR]
            eventDate[Calendar.MONTH] = convertedDate[Calendar.MONTH]
            eventDate[Calendar.DAY_OF_MONTH] = convertedDate[Calendar.DAY_OF_MONTH]
            eventDate[Calendar.HOUR_OF_DAY] = convertedDate[Calendar.HOUR_OF_DAY]
            eventDate[Calendar.MINUTE] = convertedDate[Calendar.MINUTE]
            eventDate[Calendar.SECOND] = convertedDate[Calendar.SECOND]
            eventDate.timeZone = TimeZone.getTimeZone("Asia/Tehran")

            // Find how many milliseconds until the event
            val diff = eventDate.timeInMillis - currentDate.timeInMillis

            // Change the milliseconds to days, hours, minutes and seconds
            val days = diff / (24 * 60 * 60 * 1000)
            val hours = diff / (1000 * 60 * 60) % 24
            val minutes = diff / (1000 * 60) % 60
            val seconds = (diff / 1000) % 60

            // Display Countdown
            mBinding.tvCountdown.text = if(days>0) "${days}روز و ${hours}ساعت و ${minutes}دقیقه و ${seconds}ثانیه " else
                if(hours>0) "${hours}ساعت و ${minutes}دقیقه و ${seconds}ثانیه " else
                    if(minutes>0) "${minutes}دقیقه و ${seconds}ثانیه " else
                        "${seconds}ثانیه "

            // Show different text when the event has passed
            endEvent(currentDate, eventDate)
        }

        private fun endEvent(currentdate: Calendar, eventdate: Calendar) {
            if (currentdate.timeInMillis >= eventdate.timeInMillis) {
                mBinding.tvCountdown.text = "منقضی شده"
                //Stop Handler
                handler.removeMessages(0)
            }
        }
    }

    init {
        dataList = personalItemViewModelList
        //       notifyDataSetChanged();
    }
}
